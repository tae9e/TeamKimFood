import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';

const MyPost = () => {
    const { id } = useParams();
    const [post, setPost] = useState(null);

    // 포스트 정보 가져오기
    useEffect(() => {
        const fetchPost = async () => {
            try {
                const response = await axios.get(`/mypage/myposts/${id}`);
                setPost(response.data);
            } catch (error) {
                console.error('포스트 정보를 불러오는 중 에러 발생:', error);
            }
        };

        fetchPost();
    }, [id]); // id가 변경될 때마다 useEffect가 실행

    // 포스트 삭제
    const deletePost = async () => {
        try {
            await axios.delete(`/mypage/myposts/${id}`); // URL 파라미터를 사용하여 API 호출
            console.log('포스트 삭제 성공');
            setPost(null); // 삭제 후 포스트 정보를 null로 설정하여 화면에서 사라지도록 함
        } catch (error) {
            console.error('포스트 삭제 중 에러 발생:', error);
        }
    };

    return (
        <div>
            {post ? (
                <div>
                    <h1>{post.title}</h1>
                    <p>작성자 ID: {post.memberId}</p>
                    <p>레시피 ID: {post.recipeId}</p>
                    <button onClick={deletePost}>포스트 삭제</button>
                </div>
            ) : (
                <p>포스트를 불러오는 중...</p>
            )}
        </div>
    );
};

export default MyPost;