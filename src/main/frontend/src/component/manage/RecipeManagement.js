import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const RecipeManagement = () => {
    const [recipes, setRecipes] = useState([]);
    const navigate = useNavigate();

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const day = date.getDate().toString().padStart(2, '0');
        return `${year}년${month}월${day}일`;
    };

    useEffect(() => {
        const fetchRecipes = async () => {
            try {
                const response = await axios.get(`/admin/dashboard/recipeManagement`);
                setRecipes(response.data);
            } catch (err) {
                console.error('레시피 목록을 불러오는 데 실패했습니다.', err);
                // 오류 처리
            }
        };

        fetchRecipes();
    }, []);

    const handleRecipeDelete = async (id) => {
        if (window.confirm('레시피를 정말 삭제하시겠습니까?')) {
            try {
                await axios.delete(`/api/recipes/${id}/delete`);
                setRecipes(recipes.filter(recipe => recipe.id !== id));
            } catch (err) {
                console.error('레시피 삭제에 실패했습니다.', err);
                // 오류 처리
            }
        }
    };

    const handleRecipeDetail = (id) => {
        navigate(`/api/recipe/${id}`);
    };

    return (
        <div>
            <h2>레시피 관리</h2>
            <table>
                <thead>
                <tr>
                    <th>번호</th>
                    <th>타이틀</th>
                    <th>닉네임</th>
                    <th>조회수</th>
                    <th>작성일</th>
                    <th>작업</th>
                </tr>
                </thead>
                <tbody>
                {recipes.map((recipe, index) => (
                    <tr key={recipe.id}>
                        <td>{index + 1}</td>
                        <td style={{ cursor: 'pointer' }} onClick={() => handleRecipeDetail(recipe.id)}>
                            {recipe.title}
                        </td>
                        <td>{recipe.nickname}</td>
                        <td>{recipe.viewCount}</td>
                        <td>{formatDate(recipe.writeDate)}</td>
                        <td>
                            <button onClick={() => handleRecipeDelete(recipe.id)}>
                                삭제
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default RecipeManagement;