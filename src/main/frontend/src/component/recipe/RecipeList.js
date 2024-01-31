import React, { useState, useEffect } from 'react';
import axios from 'axios';

const RecipeList = () => {
    const [recipes, setRecipes] = useState([]);
    const [page, setPage] = useState(0);

    useEffect(() => {
        const fetchRecipes = async () => {
            const response = await axios.get(`/api/recipes/boardList?page=${page}`);
            setRecipes(response.data.content);
        };

        fetchRecipes();
    }, [page]);

    return (
        <div>
            {recipes.map((recipe, index) => (
                <RecipeItem key={recipe.id} recipe={recipe} />
            ))}
            <Pagination currentPage={page} setPage={setPage} />
        </div>
    );
};

const RecipeItem = ({ recipe }) => {
    console.log("이미지 : ", recipe.imgUrl);
    const imageUrl = `${process.env.PUBLIC_URL}${recipe.imgUrl}`;
    if (!recipe){
        return (<div>표시할 레시피가 없습니다.</div>);
    } else {
        return (
            <div>
                <h3>{recipe.title}</h3>
                <img src={imageUrl} alt={recipe.title} />
                <div>조회수: {recipe.viewCount}</div>
                <div>{recipe.nickName}</div>
                <div>{recipe.writeDate}</div>
            </div>
        );
    }
};

const Pagination = ({ currentPage, setPage }) => {
    return (
        <div>
            <button onClick={() => setPage(currentPage - 1)}>이전</button>
            <button onClick={() => setPage(currentPage + 1)}>다음</button>
        </div>
    );
};

export default RecipeList;