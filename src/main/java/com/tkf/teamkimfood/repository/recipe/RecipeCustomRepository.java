package com.tkf.teamkimfood.repository.recipe;

import com.tkf.teamkimfood.dto.MainpageRecipeDto;
import com.tkf.teamkimfood.dto.aboutrecipe.RecipeSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipeCustomRepository {
    Page<MainpageRecipeDto> getMainRecipePage(RecipeSearchDto recipeSearchDto, Pageable pageable);
}
