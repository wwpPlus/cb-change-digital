package com.cb.changedigital.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cb.changedigital.common.ApiResponse;
import com.cb.changedigital.entity.DictionaryItem;
import com.cb.changedigital.mapper.DictionaryItemMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dictionaries")
public class DictionaryController {

    private final DictionaryItemMapper dictionaryItemMapper;

    public DictionaryController(DictionaryItemMapper dictionaryItemMapper) {
        this.dictionaryItemMapper = dictionaryItemMapper;
    }

    @GetMapping("/{type}")
    public ApiResponse<List<DictionaryItem>> listByType(@PathVariable String type) {
        return ApiResponse.ok(dictionaryItemMapper.selectList(new LambdaQueryWrapper<DictionaryItem>()
                .eq(DictionaryItem::getDictType, type)));
    }
}
