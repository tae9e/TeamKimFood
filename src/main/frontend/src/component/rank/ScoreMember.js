import React, {useEffect, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";

function ScoreMember(){
    const [inputData, setInputData] = useState([]);

    useEffect(()=>{
        const fetchRecipes = async () => {
            try {
                const res = await axios.get(`http://localhost:8080/api/rank/users`);
                setInputData(res.data);
            } catch (e) {
                console.error(e.message);
            }
        };
        fetchRecipes();
    }, []);


    return(
        <div>
            <h3>유저 랭킹</h3>
            <div>
                <table >
                    <tbody>
                    <tr>
                        <td >닉네임</td>
                        <td >총추천수</td>
                    </tr>
                    {inputData.length > 0 ? (
                        inputData.map(rowData => (
                            <tr key={rowData.id}>
                                <td>
                                    {rowData.nickname}
                                </td>
                                <td>
                                    {rowData.totalScore}
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td ></td>
                            <td >충분한 데이터가 없습니다.</td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
            {/*{Array.from({length: totalPages}, (_, index) => (*/}
            {/*    <button key={index} onClick={() => handlePageChange(index)}>*/}
            {/*        {index + 1}*/}
            {/*    </button>*/}
            {/*))}*/}
        </div>
    );
}
export default ScoreMember;