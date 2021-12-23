import React from 'react'
import { Tabs } from 'antd'
import ParcelHDList from '../components/ParcelHDList.component';
import ParcelDTList from '../components/ParcelDTList.component';
import ParcelCaveatList from '../components/ParcelCaveatList.component';
import ParcelBurList from '../components/ParcelBurList.component';
import ParcelM9List from '../components/ParcelM9List.component';
import ParcelConList from '../components/ParcelConList.component';

import '../styles/datatable.css';
import '../styles/overview.css';

const { TabPane } = Tabs;

export default function Overview() {
    return (
        <div className='overview'>
            <h3>ยอดสรุปข้อผิดพลาด</h3>

            <Tabs tabPosition='left' type="card" className='overview-tabs'>
                <TabPane tab="โฉนดที่ดิน" key="chanode-tab">
                    <Tabs type="card">
                        <TabPane tab="ข้อมูลเอกสารสิทธิ" key="1">
                            <ParcelHDList printplate_type={0} />
                        </TabPane>
                        <TabPane tab="ข้อมูลผู้ถือกรรมสิทธิ์" key="2">
                            <ParcelDTList printplate_type={0} />
                        </TabPane>
                        <TabPane tab="ข้อมูลผู้อายัด" key="3">
                            <ParcelCaveatList printplate_type={0} />
                        </TabPane>
                        <TabPane tab="ข้อมูลภาระผูกพัน" key="4">
                            <ParcelBurList printplate_type={0} />
                        </TabPane>
                        <TabPane tab="ข้อมูลผู้จัดการมรดก" key="5">
                            <ParcelM9List printplate_type={0} />
                        </TabPane>
                        <TabPane tab="ข้อมูลสิ่งปลูกสร้าง" key="6">
                            <ParcelConList printplate_type={0} />
                        </TabPane>
                    </Tabs>
                </TabPane>
                <TabPane tab="โฉนดตราจอง" key="chanodetrajong-tab">
                    <Tabs type="card">
                        <TabPane tab="ข้อมูลเอกสารสิทธิ" key="1">
                            <ParcelHDList printplate_type={1} />
                        </TabPane>
                        <TabPane tab="ข้อมูลผู้ถือกรรมสิทธิ์" key="2">
                            <ParcelDTList printplate_type={1} />
                        </TabPane>
                        <TabPane tab="ข้อมูลผู้อายัด" key="3">
                            <ParcelCaveatList printplate_type={1} />
                        </TabPane>
                        <TabPane tab="ข้อมูลภาระผูกพัน" key="4">
                            <ParcelBurList printplate_type={1} />
                        </TabPane>
                        <TabPane tab="ข้อมูลผู้จัดการมรดก" key="5">
                            <ParcelM9List printplate_type={1} />
                        </TabPane>
                        <TabPane tab="ข้อมูลสิ่งปลูกสร้าง" key="6">
                            <ParcelConList printplate_type={1} />
                        </TabPane>
                    </Tabs>
                </TabPane>
                <TabPane tab="ตราจองที่ได้ทำประโยชน์แล้ว" key="trajong-tab">
                    <Tabs type="card">
                        <TabPane tab="ข้อมูลเอกสารสิทธิ" key="1">
                            <ParcelHDList printplate_type={2} />
                        </TabPane>
                        <TabPane tab="ข้อมูลผู้ถือกรรมสิทธิ์" key="2">
                            <ParcelDTList printplate_type={2} />
                        </TabPane>
                        <TabPane tab="ข้อมูลผู้อายัด" key="3">
                            <ParcelCaveatList printplate_type={2} />
                        </TabPane>
                        <TabPane tab="ข้อมูลภาระผูกพัน" key="4">
                            <ParcelBurList printplate_type={2} />
                        </TabPane>
                        <TabPane tab="ข้อมูลผู้จัดการมรดก" key="5">
                            <ParcelM9List printplate_type={2} />
                        </TabPane>
                        <TabPane tab="ข้อมูลสิ่งปลูกสร้าง" key="6">
                            <ParcelConList printplate_type={2} />
                        </TabPane>
                    </Tabs>
                </TabPane>
                <TabPane tab="น.ส.3" key="ns3-tab">
                    <Tabs type="card">
                        <TabPane tab="ข้อมูลเอกสารสิทธิ" key="1">
                            <ParcelHDList printplate_type={3} />
                        </TabPane>
                        <TabPane tab="ข้อมูลผู้ถือกรรมสิทธิ์" key="2">
                            <ParcelDTList printplate_type={3} />
                        </TabPane>
                        <TabPane tab="ข้อมูลผู้อายัด" key="3">
                            <ParcelCaveatList printplate_type={3} />
                        </TabPane>
                        <TabPane tab="ข้อมูลภาระผูกพัน" key="4">
                            <ParcelBurList printplate_type={3} />
                        </TabPane>
                        <TabPane tab="ข้อมูลผู้จัดการมรดก" key="5">
                            <ParcelM9List printplate_type={3} />
                        </TabPane>
                        <TabPane tab="ข้อมูลสิ่งปลูกสร้าง" key="6">
                            <ParcelConList printplate_type={3} />
                        </TabPane>
                    </Tabs>
                </TabPane>
                <TabPane tab="น.ส.3ก" key="ns3a-tab">
                    <Tabs type="card">
                        <TabPane tab="ข้อมูลเอกสารสิทธิ" key="1">
                            <ParcelHDList printplate_type={4} />
                        </TabPane>
                        <TabPane tab="ข้อมูลผู้ถือกรรมสิทธิ์" key="2">
                            <ParcelDTList printplate_type={4} />
                        </TabPane>
                        <TabPane tab="ข้อมูลผู้อายัด" key="3">
                            <ParcelCaveatList printplate_type={4} />
                        </TabPane>
                        <TabPane tab="ข้อมูลภาระผูกพัน" key="4">
                            <ParcelBurList printplate_type={4} />
                        </TabPane>
                        <TabPane tab="ข้อมูลผู้จัดการมรดก" key="5">
                            <ParcelM9List printplate_type={4} />
                        </TabPane>
                        <TabPane tab="ข้อมูลสิ่งปลูกสร้าง" key="6">
                            <ParcelConList printplate_type={4} />
                        </TabPane>
                    </Tabs>
                </TabPane>
                <TabPane tab="น.ส.ล." key="nsl-tab">
                    <Tabs type="card">
                        <TabPane tab="ข้อมูลเอกสารสิทธิ" key="1">
                            <ParcelHDList printplate_type={8} />
                        </TabPane>
                    </Tabs>
                </TabPane>
                <TabPane tab="หนังสืออนุญาต" key="snsl-tab">
                    <Tabs type="card">

                    </Tabs>
                </TabPane>
                <TabPane tab="อาคารชุด" key="condo-tab">
                    <Tabs type="card">

                    </Tabs>
                </TabPane>
                <TabPane tab="ห้องชุด" key="condoroom-tab">
                    <Tabs type="card">
                        <TabPane tab="ข้อมูลเอกสารสิทธิ" key="1">
                            <ParcelHDList printplate_type={9} />
                        </TabPane>
                        <TabPane tab="ข้อมูลผู้ถือกรรมสิทธิ์" key="2">
                            <ParcelDTList printplate_type={9} />
                        </TabPane>
                        <TabPane tab="ข้อมูลผู้อายัด" key="3">
                            <ParcelCaveatList printplate_type={9} />
                        </TabPane>
                        <TabPane tab="ข้อมูลภาระผูกพัน" key="4">
                            <ParcelBurList printplate_type={9} />
                        </TabPane>
                    </Tabs>
                </TabPane>
            </Tabs>
        </div>
    )
}
