import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Line } from 'react-chartjs-2';
import 'chart.js/auto';

const Dashboard = () => {
    const [dashboardStats, setDashboardStats] = useState(
        { totalUsers: 0, totalRecipes: 0 });
    const [dailyStats, setDailyStats] = useState([]);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchDashboardStats = async () => {
            try {
                const response = await axios.get('/admin/dashboard/stats');
                setDashboardStats(response.data);
            } catch (err) {
                setError('대시보드 통계를 불러오는 데 실패했습니다.');
                console.error('대시보드 통계 로드 오류:', err);
            }
        };

        const fetchDailyStats = async () => {
            try {
                const response = await axios.get('/admin/dashboard/stats/daily');
                setDailyStats(response.data);
            } catch (err) {
                setError('일별 활동 통계를 불러오는 데 실패했습니다.');
                console.error('일별 활동 통계 로드 오류:', err);
            }
        };

        fetchDashboardStats();
        fetchDailyStats();
    }, []);

    const dailyData = {
        labels: dailyStats.map(stat => stat.date),
        datasets: [
            {
                label: '일별 활동 수',
                data: dailyStats.map(stat => stat.count),
                fill: false,
                borderColor: 'rgb(75, 192, 192)',
                tension: 0.1
            }
        ]
    };

    return (
        <div className={'container mx-auto mt-10'}>
            <h2 className={'text-2xl font-bold mb-4'}>대시보드</h2>
            <p className={'font-semibold'}>총 사용자 수: {dashboardStats.totalUsers}</p>
            <p className={'font-semibold'}>등록된 레시피 수: {dashboardStats.totalRecipes}</p>
            <div className={'mb-6'}>
            <h3 className={'text-xl font-bold mb-3'}>일별 활동 통계</h3>
            <Line data={dailyData} />
            </div>
        </div>
    );
};

export default Dashboard;