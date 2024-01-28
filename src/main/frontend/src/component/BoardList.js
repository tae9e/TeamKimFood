import React, {  useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from "axios";

function BoardList() {

        const [inputData, setInputData] = useState([{
            view_count: '',
            // correction_date: '', //수정일은 따로 안띄워도 될 것 같습니다.
            nickname: '',
            recipe_id: '',
            score: '',
            write_date: '',
            // content: '',
            title: ''
            //imgUrl도 추가 하셔야 할 것 같습니다.
        }])

    // 글 리스트의 갯수를 세기 위해 선언, 기본값 0
    const [lastIdx, setLastIdx] = useState(0)

        // await 를 사용하기 위해 async선언
        useEffect(async () => {
            try {
                // 데이터를 받아오는 동안 시간이 소요되므로 await 로 대기
                const res = await axios.get('/api/boardlist')

                // 받아온 데이터로 다음 작업을 진행하기 위해 await 로 대기
                // 받아온 데이터를 map 해주어 rowData 별로 _inputData 선언
                const _inputData = await res.data.map((rowData) => ({
                    view_count: rowData.view_count,
                    // correction_date: rowData.correction_date, //수정일은 따로 안띄워도 될 것 같습니다.
                    nickname: rowData.nickname,//member_id 받으시면 큰일납니다!
                    recipe_id: rowData.recipe_id,
                    write_date: rowData.write_date,
                    // content: rowData.content,
                    title: rowData.title
                    })
                )
                // 선언된 _inputData 를 최초 선언한 inputData 에 concat 으로 추가
                setInputData(inputData.concat(_inputData))
            } catch (e) {
                console.error(e.message)
            }
        }, [])

    return (
            <div>
                <h3>게시판</h3>
                <div>
                    <table className='listTable'>
                        <tbody>
                        <tr>
                            <td className='listTableIndex th'>index</td>
                            <td className='listTableTitle th'>title</td>
                        </tr>
                        {/*rowData 가 없으면 '작성된 글이 없습니다'를 나타냄*/}
                        {lastIdx !== 0 ?
                            inputData.map(rowData => (
                                // 최초 선언한 기본값은 나타내지 않음
                                rowData.recipe_id !== '' &&
                                <tr>
                                    <td className='listTableIndex'>
                                        {/*router 로 이동 시 idx값을 param 으로 전달*/}
                                        <Link to={`/BoardContent/${rowData.recipe_id}`}>{rowData.recipe_id}</Link>
                                    </td>
                                    <td className='listTableTitle'>
                                        <Link to={`/BoardContent/${rowData.recipe_id}`}>{rowData.title}</Link>
                                    </td>
                                    <td>{rowData.nickname}</td>
                                    <td>{rowData.view_count}</td>
                                    <td>{rowData.write_date}</td>
                                {/*    imgUrl 추가하는 html코드 추가해주세요! */}
                                </tr>

                            )) :
                            <tr>
                                <td className='listTableIndex'></td>
                                <td className='listTableTitle noData'>작성된 글이 없습니다.</td>
                            </tr>
                        }
                        </tbody>
                    </table>
                </div>
            </div>
        )
}
    export default BoardList;