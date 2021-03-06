package com.ssu.project.service.item;

import com.ssu.project.domain.category.ChildCategory;
import com.ssu.project.domain.category.ChildCategoryRepository;
import com.ssu.project.domain.category.ParentCategory;
import com.ssu.project.domain.category.ParentCategoryRepository;
import com.ssu.project.domain.item.Item;
import com.ssu.project.domain.item.ItemRepository;
import com.ssu.project.dto.item.ItemRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ItemService {
    private final ItemRepository itemRepository;
    private final ParentCategoryRepository parentCategoryRepository;
    private final ChildCategoryRepository childCategoryRepository;

    @PostConstruct
    public void initAlbumItems() throws IOException, ParseException {
        Resource resource = new ClassPathResource("items.json");

        JSONParser parser = new JSONParser();

        String uri = resource.getFile().toPath().toString();

        JSONArray obj = (JSONArray) parser.parse(new FileReader(uri));

        for (int i = 0; i < obj.size(); i++) {
            JSONObject object = (JSONObject) obj.get(i);
            Item item = new Item();
            item.setId((long) object.get("id"));
            item.setBrand((String) object.get("brand"));
            item.setName((String) object.get("name"));
            item.setPrice((long) object.get("price"));
            item.setCode((String) object.get("item_code"));
            item.setImg_name((String) object.get("img_name"));

            ParentCategory parent = parentCategoryRepository.findByName((String)object.get("big_category"));

            if (parent == null) {
                ParentCategory newParent = new ParentCategory();
                newParent.setName((String) object.get("big_category"));
                item.setParentCategory(newParent);
            } else {
                item.setParentCategory(parent);
            }

            ChildCategory child = childCategoryRepository.findByname((String) object.get("small_category"));
            if (child == null) {
                ChildCategory newChild = new ChildCategory();
                newChild.setName((String) object.get("small_category"));
                item.setChildCategory(newChild);
            } else {
                item.setChildCategory(child);
            }

            JSONArray urlArr = (JSONArray) object.get("detail_img");

            Iterator<String> iterator1 = urlArr.iterator();
            while (iterator1.hasNext()) {
                item.getUrls().add(iterator1.next());
            }

            JSONArray sizeArr = (JSONArray) object.get("size");

            Iterator<String> iterator2 = sizeArr.iterator();

            while (iterator2.hasNext()) {
                item.getSizes().add(iterator2.next());
            }
            itemRepository.save(item);
        }
    }

    public List<Item> getItemList() {
        return itemRepository.findAll();
    }

    /**
     * ????????????
     * @param keyword
     * @return
     */
    public List<Item> findByAllKeyword(String keyword) {
        return itemRepository.findByAllKeyword(keyword);
    }

    /**
     * name??? ??????
     * @param keyword
     * @return
     */
    public List<Item> findByNameKeyword(String keyword) {
        return itemRepository.findByNameKeyword(keyword);
    }

    /**
     * brand??? ??????
     * @param keyword
     * @return
     */
    public List<Item> findByBrandKeyword(String keyword) {
        return itemRepository.findByBrandKeyword(keyword);
    }

    /**
     * ???????????? ????????? ??????
     * @param category
     * @param pageable
     * @return
     */
    public List<Item> getItemListByCategory(String category, Pageable pageable) {
        if (category.equals("best")) {
            return itemRepository.findItem(pageable);
        } else if (category.indexOf("_") > -1) {
            return itemRepository.findItemByParentCategory(category.split("_")[0], category.split("_")[1], pageable);
        } else {
            return itemRepository.findItemByParentCategory(category, pageable);
        }
    }

    public Long getCountItemListByCategory(String category) {
        if (category.equals("best")) {
            return itemRepository.count();
        } else if (category.indexOf("_") > -1) {
            return itemRepository.countItemByParentCategory(category.split("_")[0], category.split("_")[1]);
        } else {
            return itemRepository.countItemByParentCategory(category);
        }
    }

    /**
     * ????????? ????????? ??????????????? ??? ?????? ????????????
     * @param itemRequestDto
     * @return
     */
    public Pageable getPageable(ItemRequestDto itemRequestDto) {
        String sort = itemRequestDto.getSort() != null ? itemRequestDto.getSort() : "liked";
        int page = itemRequestDto.getPage() - 1;
        int limit = itemRequestDto.getLimit();

        if (limit == 0) {
            limit = 20;
        }

        Sort sortBy = null;

        if (sort.equals("price_high")) {
            sortBy = Sort.by(Sort.Direction.DESC, "price");
        } else if (sort.equals("price_low")) {
            sortBy = Sort.by(Sort.Direction.ASC, "price");
        } else {
            sortBy = Sort.by(Sort.Direction.ASC, sort);
        }
        return PageRequest.of(page, limit, sortBy);
    }

    /**
     * ?????? 1. getOne(id) ??????
     * ?????? 2. findById(id) ??????
     * @param id
     * @return
     */
    public Item findItem(Long id) {
        Optional<Item> optional = itemRepository.findById(id);
        return optional.orElseGet(() -> itemRepository.findById(id).get());
    }

    /**
     * ?????? ???(Recomend Dress Code)
     * ?????????(Outer), ??????(Top), ??????(bottom)
     * @param parent
     * @param child
     * @return
     */
    public Item findRecommendCategory(Long parent, Long child){
        ParentCategory parentCategory = parentCategoryRepository.getOne(parent);

        ChildCategory childCategory = childCategoryRepository.getOne(child);

        List<Item> itemList = itemRepository.findAllByParentCategoryAndChildCategory(parentCategory, childCategory);

        Item item = null;

        if (itemList.size()>0) {
            int index = (int)(Math.random() * itemList.size());

            item = itemList.get(index);

            item.setMainUrl(item.getUrls().get(0));
        }
        return item;
    }
}