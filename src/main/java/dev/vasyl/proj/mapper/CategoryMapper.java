package dev.vasyl.proj.mapper;

import dev.vasyl.proj.config.MapperConfig;
import dev.vasyl.proj.dto.category.CategoryDto;
import dev.vasyl.proj.dto.category.CreateCategoryRequestDto;
import dev.vasyl.proj.model.Category;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {

    Category toModel(CreateCategoryRequestDto categoryRequestDto);

    Category toModel(Long id);

    default Long toId(Category category) {
        return category != null ? category.getId() : null;
    }

    @Named("toIdList")
    default List<Long> toIdList(Set<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return new ArrayList<>();
        }
        return categories.stream()
                .map(this::toId)
                .toList();
    }

    @Named("toModelList")
    default Set<Category> toModelList(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        return ids.stream()
                .map(this::toModel)
                .collect(Collectors.toSet());
    }

    void updateCategoryFromDto(CreateCategoryRequestDto requestDto, @MappingTarget Category entity);

    CategoryDto toDto(Category category);

    default Page<CategoryDto> toCategoryDtoPage(Page<Category> categoryPage) {
        List<CategoryDto> dtoList = categoryPage.getContent().stream()
                .map(this::toDto)
                .toList();
        return new PageImpl<>(dtoList, categoryPage.getPageable(), categoryPage.getTotalElements());
    }
}
