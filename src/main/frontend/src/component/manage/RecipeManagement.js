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
        <div className={'container mx-auto mt-10 border p-3 rounded-lg'}>
            <h2 className={'text-xl font-bold mb-4'}>레시피 관리</h2>
            <table className={'min-w-full table-auto border-collapse'}>
                <thead>
                <tr>
                    <th className={'border px-4 py-2'}>번호</th>
                    <th className={'border px-4 py-2'}>타이틀</th>
                    <th className={'border px-4 py-2'}>닉네임</th>
                    <th className={'border px-4 py-2'}>조회수</th>
                    <th className={'border px-4 py-2'}>작성일</th>
                    <th className={'border px-4 py-2'}>작업</th>
                </tr>
                </thead>
                <tbody>
                {recipes.map((recipe, index) => (
                    <tr key={recipe.id} className={'hover:bg-gray-50'}>
                        <td className={'border px-4 py-2'}>{index + 1}</td>
                        <td className={'border px-4 py-2 cursor-pointer'} style={{ cursor: 'pointer' }} onClick={() => handleRecipeDetail(recipe.id)}>
                            {recipe.title}
                        </td>
                        <td className={'border px-4 py-2'}>{recipe.nickname}</td>
                        <td className={'border px-4 py-2'}>{recipe.viewCount}</td>
                        <td className={'border px-4 py-2'}>{formatDate(recipe.writeDate)}</td>
                        <td className={'border px-4 py-2'}>
                            <button onClick={() => handleRecipeDelete(recipe.id)}
                            className={'bg-red-500 hover:bg-red-700 text-white font-bold py-1 px-3 rounded'}>
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