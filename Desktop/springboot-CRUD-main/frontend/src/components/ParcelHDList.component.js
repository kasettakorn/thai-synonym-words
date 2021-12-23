
import { Spin } from 'antd';
import React, { useEffect, useState } from 'react'
import DataTable from './DataTable.component';


function ParcelHDList({ printplate_type }) {
    const [overview, setOverview] = useState(null);


    useEffect(() => {

        async function fetchData() {
            var url = '';
            switch (printplate_type) {
                case 0:
                case 1:
                case 2:
                    url = "http://localhost:9092/overview/hd/561/" + printplate_type;
                    break;
                case 3:
                    url = "http://localhost:9092/overview/ns3hd/561/";
                    break;
                case 4:
                    url = "http://localhost:9092/overview/ns3ahd/561/";
                    break;
                case 8:
                    url = "http://localhost:9092/overview/nsl/561/";
                    break;
                case 23:
                    url = "http://localhost:9092/overview/snsl/561/";
                    break;
                case 9:
                    url = "http://localhost:9092/overview/unit_condo/561/";
                    break;
                case 13:
                    url = "http://localhost:9092/overview/condo/561/";
                    break;
                default:
                    break;
            }

            const response = await fetch(url)
                .then(response => response.json())
                .then(data => {
                    return data;
                });

            switch (printplate_type) {
                case 3: //ns3
                    setOverview([
                        {
                            problem_desc: 'เลขที่ น.ส. 3 ไม่ถูกต้อง (เลขที่ น.ส. 3 ต้องเป็นตัวเลขเท่านั้น)',
                            value: response[0].nan_NS3ID
                        },
                        {
                            problem_desc: 'ตรวจสอบเนื้อที่ "งาน" เกิน 3 งาน',
                            value: response[0].overflow_NNHAN
                        },
                        {
                            problem_desc: 'ตรวจสอบเนื้อที่ "ตารางวา" เกิน 99 ตารางวา',
                            value: response[0].overflow_NWAH
                        }
                    ]);
                    break;
                case 4: //ns3a
                    setOverview([
                        {
                            problem_desc: 'เลขที่ น.ส.3ก. ไม่ถูกต้อง (เลขที่ น.ส.3ก. ต้องเป็นตัวเลขเท่านั้น)',
                            value: response[0].nan_NS3AID
                        },
                        {
                            problem_desc: 'ตรวจสอบเนื้อที่ "งาน" เกิน 3 งาน',
                            value: response[0].overflow_NNHAN
                        },
                        {
                            problem_desc: 'ตรวจสอบเนื้อที่ "ตารางวา" เกิน 99 ตารางวา',
                            value: response[0].overflow_NWAH
                        },
                        {
                            problem_desc: 'ไม่มีเลขที่ดิน',
                            value: response[0].no_LANDNO
                        },
                        {
                            problem_desc: 'หมายเลขระวาง UTM ผิดรูปแบบ',
                            value: response[0].invalid_UTM_FORMAT
                        },
                        {
                            problem_desc: 'มาตราส่วนไม่ถูกต้อง (ต้องเป็นมาตราส่วน 5000 เท่านั้น)',
                            value: response[0].invalid_RATIO
                        }
                    ]);
                    break;
                case 8:
                    setOverview([
                        {
                            problem_desc: 'ไม่มีรหัสตำบล',
                            value: response[0].no_TUMBON_CODE
                        },
                        {
                            problem_desc: 'รหัสตำบลไม่ถูกต้องตรงตามข้อมูลในพื้นที่ตำบลนั้น',
                            value: response[0].invalid_TUMBON_CODE
                        },
                        {
                            problem_desc: 'ปี น.ส.ล. มีตัวอักษร (ต้องเป็นตัวเลขเท่านั้น)',
                            value: response[0].invalid_NOSORO_YEAR
                        },
                        {
                            problem_desc: 'ปี น.ส.ล. ไม่อยู่ระหว่างปี 2460 ถึง ปัจจุบัน',
                            value: response[0].invalid_NOSORO_YEAR
                        },
                        {
                            problem_desc: 'เลขที่ น.ส.ล. ไม่ถูกต้อง (เลขที่ น.ส.ล. ต้องเป็นตัวเลขเท่านั้น)',
                            value: response[0].nan_NOSORO_NO
                        },
                        {
                            problem_desc: 'ตรวจสอบเนื้อที่ "งาน" เกิน 3 งาน',
                            value: response[0].overflow_NGAN
                        },
                        {
                            problem_desc: 'ตรวจสอบเนื้อที่ "ตารางวา" เกิน 99 ตารางวา',
                            value: response[0].overflow_WA
                        },
                        {
                            problem_desc: 'ไม่มีเลขที่ดิน',
                            value: response[0].no_LANDNO
                        },
                        {
                            problem_desc: 'หมายเลขระวาง UTM ผิดรูปแบบ',
                            value: response[0].invalid_UTM_FORMAT
                        },
                        {
                            problem_desc: 'มาตราส่วนไม่ถูกต้อง (ต้องเป็นมาตราส่วน 500, 1000, 2000, 4000, 5000 เท่านั้น',
                            value: response[0].invalid_RAVANG_RATIO
                        }
                    ]);
                    break;
                case 9:
                    setOverview([
                        {
                            problem_desc: 'เลขทะเบียนอาคารชุดผิดรูปแบบ (เลขลำดับ/ปี พ.ศ.)',
                            value: response[0].invalid_REG_NO
                        },
                        {
                            problem_desc: 'ไม่มีชื่ออาคาร',
                            value: response[0].no_BLD_NO
                        }
                    ]);
                    break;
                default:
                    setOverview([
                        {
                            problem_desc: 'ไม่ได้ใส่ข้อมูลประเภทเอกสารสิทธิ',
                            value: response[0].no_DATA
                        },
                        {
                            problem_desc: 'ใส่ข้อมูลประเภทเอกสารสิทธิผิด นอกเหนือจาก โฉนดที่ดิน, ตราจอง, โฉนดตราจอง',
                            value: response[0].wrongtype
                        },
                        {
                            problem_desc: 'ไม่ได้ใส่รหัสตำบล',
                            value: response[0].no_TAMBON_CODE
                        },
                        {
                            problem_desc: 'รหัสตำบลไม่ถูกต้องตรงตามข้อมูลในพื้นที่ตำบลนั้น',
                            value: response[0].no_AREA_TUMBON_CODE
                        },
                        {
                            problem_desc: 'เลขที่โฉนดไม่ถูกต้อง (เลขที่โฉนดต้องเป็นตัวเลขเท่านั้น)',
                            value: response[0].nan_PARCEL_ID
                        }, {
                            problem_desc: 'ไม่มีหน้าสำรวจ',
                            value: response[0].no_SURVEY
                        }, {
                            problem_desc: 'เลขที่หน้าสำรวจไม่ถูกต้อง (เลขที่หน้าสำรวจต้องเป็นตัวเลขเท่านั้น)',
                            value: response[0].nan_SURVEY
                        }, {
                            problem_desc: 'ตรวจสอบพบเนื้อที่ "งาน" เกิน 3 งาน',
                            value: response[0].more_3NNHAN
                        }, {
                            problem_desc: 'ตรวจสอบพบเนื้อที่ "ตารางวา" เกิน 99 ตารางวา',
                            value: response[0].more_99NWAH
                        }, {
                            problem_desc: 'ไม่มีเลขที่ดิน',
                            value: response[0].no_UTM_LANDNO
                        }, {
                            problem_desc: 'หมายเลขระวาง UTM ผิดรูปแบบ',
                            value: response[0].wrong_UTM_FORMAT
                        }, {
                            problem_desc: 'มาตราส่วนไม่ถูกต้อง (ต้องเป็นมาตราส่วน 500, 1000, 2000, 4000 เท่านั้น)',
                            value: response[0].wrong_UTM_RATIO
                        }, {
                            problem_desc: 'มาตราส่วนของระวางแผนที่ศูนย์กำเนิดไม่ถูกต้อง (ใส่มาตราส่วน 500, 1000, 2000, 4000,5000 เท่านั้น)',
                            value: response[0].wrong_RATIO
                        }
                    ]);

                    break;
            }

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

export default ParcelHDList
