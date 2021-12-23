import { Spin } from 'antd';
import React, { useEffect, useState } from 'react'
import DataTable from './DataTable.component';

function ParcelCaveatList({ printplate_type }) {
    const [overview, setOverview] = useState(null);


    useEffect(() => {
        async function fetchData() {

            let url = '';
            switch (printplate_type) {
                case 0:
                case 1:
                case 2:
                    url = "http://localhost:9092/overview/caveat/561/"+printplate_type;
                    break;
                case 3:
                    url = "http://localhost:9092/overview/ns3_caveat/561/";
                    break;
                case 4:
                    url = "http://localhost:9092/overview/ns3a_caveat/561/";
                    break;
                case 9:
                    url = "http://localhost:9092/overview/condoroom_caveat/561/";
                    break;
                default:
                    break;
            }

            const response = await fetch(url)
                .then(response => response.json())
                .then(data => {
                    return data;
                });
            console.log(response);
            setOverview([
                {
                    problem_desc: 'ไม่มีชื่อผู้ขออายัด',
                    value: response[0].no_CAVEAT_NAME
                },
                {
                    problem_desc: 'ชื่อผู้ขออายัดมีตัวอักษรมากกว่า 200 อักขระเกินโครงสร้างที่จัดเก็บของระบบทะเบียน',
                    value: response[0].overflow_CAVAET_NAME
                },
                {
                    problem_desc: 'ไม่มีวันที่ขออายัด',
                    value: response[0].no_CAVEAT_DATE
                },
                {
                    problem_desc: 'ไม่มีวันที่รับอายัด',
                    value: response[0].no_EN_DATE
                },
                {
                    problem_desc: 'ไม่มีวันที่ถอนอายัด',
                    value: response[0].no_CAN_DATE
                },
                {
                    problem_desc: 'สถานะอายัดเป็นค่าว่าง',
                    value: response[0].no_CAVEAT_FLG
                },
                {
                    problem_desc: 'สถานะอายัดไม่ถูกต้อง (สถานะอายัด 1 คือ อายัด, D คือ ถอนอายัด)',
                    value: response[0].invalid_CAVEAT_FLG
                }

            ]);

        }
        fetchData();
    }, [])


    if (!overview) return <div style={{ position: "absolute", top: "35%", left: "50%" }}>

        <Spin size='large' />

    </div>

    
    return (
        <div>
            <DataTable data={overview} />
        </div>
    )
}

export default ParcelCaveatList
