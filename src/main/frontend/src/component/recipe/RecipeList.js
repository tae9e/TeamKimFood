import React, { useState, useEffect } from 'react';
import axios from 'axios';
import {useNavigate} from "react-router-dom";

const RecipeList = () => {
    const [recipes, setRecipes] = useState([]);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);
    const navigate = useNavigate();
    const authToken = localStorage.getItem('token');
    useEffect(() => {
        const fetchRecipes = async () => {
            try{
                const response = await axios.get(`/api/recipes/boardList?page=${page}`,{
                    headers: {
                        'Authorization': `Bearer ${authToken}`
                    }
                }
                );

                setRecipes(response.data.content);
                setTotalPages(response.data.totalPages);
            } catch (error){
                console.error('레시피 에러가 발생했습니다.', error);
            }

        };

        fetchRecipes();
    }, [page]);

    return (
        <div>
            {recipes.map((recipe, index) => (
                <RecipeItem key={recipe.id} recipe={recipe} navigate={navigate}/>
            ))}
            <Pagination currentPage={page} setPage={setPage} />
        </div>
    );
};

const RecipeItem = ({ recipe, navigate, currentPage }) => {
    console.log("이미지 : ", recipe.imgUrl);
    const imageUrl = `${process.env.PUBLIC_URL}${recipe.imgUrl}`;
    const handleTitleClick = () => {
        navigate(`/api/recipe/${recipe.id}`, { state: { fromPage: currentPage } })
    }
    if (!recipe){
        return <div>표시할 레시피가 없습니다.</div>;
    } else {
        return (
            <div>
                <h3 onClick={handleTitleClick} style={{ cursor: 'pointer' }}>{recipe.title}</h3>
                <img src={imageUrl} alt={recipe.title} />
                <div>조회수: {recipe.viewCount}</div>
                <div>{recipe.nickName}</div>
                <div>{recipe.writeDate}</div>
            </div>
        );
    }
};

const Pagination = ({ currentPage, setPage, totalPages }) => {
    // 이전 페이지 이동 함수
    const handlePrevious = () => {
        if (currentPage > 0) {
            setPage(currentPage - 1);
        } else {
            alert('이전 페이지가 없습니다.');
        }
    };

    // 다음 페이지 이동 함수
    const handleNext = () => {
        if (currentPage < totalPages - 1) {
            setPage(currentPage + 1);
        } else {
            alert('다음 페이지가 없습니다.');
        }
    };

    // 페이지 번호 버튼을 렌더링하는 함수
    const renderPageNumbers = () => {
        const pageNumbers = [];
        for (let i = 0; i < totalPages; i++) {
            pageNumbers.push(
                <button key={i} onClick={() => setPage(i)} disabled={i === currentPage}>
                    {i + 1}
                </button>
            );
        }
        return pageNumbers;
    };

    return (
        <div>
            <button onClick={handlePrevious}>이전</button>
            {renderPageNumbers()}
            <button onClick={handleNext}>다음</button>
        </div>
    );
};

export default RecipeList;