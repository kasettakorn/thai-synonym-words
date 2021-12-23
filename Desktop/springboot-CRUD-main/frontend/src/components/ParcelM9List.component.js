
import { Spin } from 'antd';
import React, { useEffect, useState } from 'react'
import DataTable from './DataTable.component';


function ParcelM9List({ printplate_type }) {
    const [overview, setOverview] = useState(null);

    useEffect(() => {
        async function fetchData() {
            let url = '';
            switch (printplate_type) {
                case 0:
                case 1:
                case 2:
                    url = "http://localhost:9092/overview/m9/561/"+printplate_type;
                    break;
                case 3:
                    url = "http://localhost:9092/overview/ns3_m9/561/";
                    break;
                case 4:
                    url = "http://localhost:9092/overview/ns3a_m9/561/";
                    break;
                case 9:
                    url = "http://localhost:9092/overview/condo_m9/561/";
                    break;
                default:
                    break;
            }

            const response = await fetch(url)
                .then(response => response.json())
                .then(data => {
                    return data;
                });
            setOverview([
                {
                    problem_desc: 'ไม่มีคำนำหน้าชื่อในข้อมูลของผู้จัดการมรดก',
                    value: response[0].no_TITLE
                },
                {
                    problem_desc: 'ไม่มีชื่อจังหวัดในข้อมูลที่อยู่ของผู้จัดการมรดก',
                    value: response[0].no_PROV
                },
                {
                    problem_desc: 'ไม่มีชื่ออำเภอในข้อมูลที่อยู่ของผู้จัดการมรดก',
                    value: response[0].no_AMPHUR
                },
                {
                    problem_desc: 'ไม่มีชื่อตำบลในข้อมูลที่อยู่ของผู้จัดการมรดก',
                    value: response[0].no_TUMB
                },
                {
                    problem_desc: 'ข้อมูลอายุไม่ถูกต้อง (ข้อมูลไม่อยู่ระหว่าง 1-999)',
                    value: response[0].invalid_AGE
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

export default ParcelM9List