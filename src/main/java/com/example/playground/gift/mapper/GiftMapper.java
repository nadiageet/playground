package com.example.playground.gift.mapper;


import com.example.playground.gift.model.Gift;
import com.example.playground.gift.response.GiftDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface GiftMapper {

    @Mapping(source = "used", target = "isUsed")
    GiftDTO mapToDto(Gift gift);
}
