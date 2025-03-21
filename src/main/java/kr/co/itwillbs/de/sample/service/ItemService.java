package kr.co.itwillbs.de.sample.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.sample.dto.ItemDTO;
import kr.co.itwillbs.de.sample.dto.ItemSearchDTO;
import kr.co.itwillbs.de.sample.entity.Item;
import kr.co.itwillbs.de.sample.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;

	public void registerItem(ItemDTO itemDto) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		itemRepository.save(itemDto.toEntity());
	}

	// ---------------------------------------------------------------------------------------
	// 리스트 검색
	
	/**
	 * 
	 * @return List<ItemDTO> 컨트롤러 들어가기전에 DTO로 변경
	 */
	public List<ItemDTO> getItemList() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		List<Item> entityList = itemRepository.findAll();
		
//		List<ItemDTO> dtoList = new ArrayList<>();
//		for(Item item: entityList) {
//			dtoList.add(item.toDto());
//		}
//		
//		return dtoList;
		
		// List<T> -> List<T2> 타입으로 변환하는 다른 방법
		// List 객체를 스트림으로 읽어들여(stream() map() 메서드를 활용하여 각 원소를 변환
		// 이 때 파라미터로 T 타입 객체를 받아 T2 타입 객체를 생성하는 코드를 map() 파라미터로 전달하고
		// 최종적으로 collect() 메서드를 호출하여 스트림 요소들을 List 타입으로 모아서 변환
		
		/*
		return entityList.stream()
		// 여기도 주석이었다.
				.map(item -> new ItemDTO(item.getId(), item.getItemNm(),
										item.getPrice(), item.getStockQty(),
										item.getItemSellStatus(), item.getItemDetail(),
										item.getRegTime(), item.getUpdateTime()) 		// 여기도 주석이었다.
				.map(item -> item.toDto()) // 각 요소 구별(item 객체를 Item 클래스의 toDto() 메서드로 ItemDTO 로 변환
				.collect(Collectors.toList());
				*/
		return convertItemToItemDTO(entityList);
	}

	/**
	 * JPA 사용에 DTO(계층간 이동)-Entity(DB접근시에만) 패턴을 위한
	 * <br>repository에서 전달 받은 List ItemEntity\\>를 List<ItemTDO>로 바꿈
	 * @param itemList
	 * @return
	 */
	private List<ItemDTO> convertItemToItemDTO(List<Item> itemList) {
		
		return itemList.stream()
				.map(item -> item.toDto())
				.collect(Collectors.toList());
	}
	
	public List<ItemDTO> getItemSearchList(ItemSearchDTO itemSearchDto) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		
		List<Item> itemList = null;
		/*
		// 1. 쉬운 버전 각각 만들기!
		if(itemSearchDto.getType().equals("itemNm")) {
			itemList = itemRepository.findByItemNmContaining(itemSearchDto.getItemNm());
			//itemList = itemRepository.findByItemNm(itemSearchDto.getItemNm());
		} else if(itemSearchDto.getType().equals("price")) {
			itemList = itemRepository.findByPriceRange(itemSearchDto.getMinPrice(), itemSearchDto.getMaxPrice());
		}
		*/
		itemList = itemRepository.findBySearchParams(itemSearchDto.getType(),
													itemSearchDto.getItemNm(), 
													itemSearchDto.getMinPrice(), 
													itemSearchDto.getMaxPrice());
		
		/*
		List<Item> itemList = itemRepository.findBySearchParams(itemSearchDto.getType(), 
																itemSearchDto.getItemNm(), 
																itemSearchDto.getMinPrice(), 
																itemSearchDto.getMaxPrice());
		*/
		return convertItemToItemDTO(itemList);
	}
	
	// ---------------------------------------------------------------------------------------
	
	public ItemDTO getItem(Long id) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return itemRepository.findById(id).get().toDto();
	}

	@Transactional
	public void modifyItem(ItemDTO itemDto) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		Item entity = itemRepository.findById(itemDto.getId()).get();
		log.debug("가져온 엔티티 확인 : {}", entity);
		// Item 객체의 값 변경 시 더티 체킹에 의해 자동으로 UPDATE 수행됨
		// => Setter 메서드를 활용하거나 별도의 메서드 추가로 정의해서 한꺼번에 변경!
		entity.changeItem(itemDto);
	}

	// 삭제 메서드는.. entity를 가져와서 지워야될줄 알았는데 JPA 녀석 너무 대단한데?
	public void removeItem(Long id) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 1차 삭제하는 방법!
		/*
		Item entity = itemRepository.findById(id).get();
		log.debug("가져온 엔티티 확인 : {}", entity);
		
		if(entity != null) {
			itemRepository.delete(entity);
		}
		*/
		
		// 1차 보다 조금 더 안전한 방법
		// Item 한 개를 조회! Optional 로!
		Optional<Item> optionalItem = itemRepository.findById(id);
		
		if(optionalItem.isPresent()) {
			// 삭제 요청
			itemRepository.delete(optionalItem.get());
		} else {
			throw new Exception("해당 상품이 존재하지 않습니다.");
		}
		
		// 잊어라.. 이녀석은
		// itemRepository.deleteById(id); //없으면 Exception 주는 위험한 녀석이었꾸나?
	}
	
}
