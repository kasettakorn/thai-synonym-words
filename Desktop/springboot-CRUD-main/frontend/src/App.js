import React, { Fragment, useEffect, useState } from 'react';
import { BrowserRouter as Router, Switch, Link, Route } from 'react-router-dom';
import './App.css';
import { Layout, Menu, Affix, BackTop, Avatar } from 'antd';
import SubMenu from 'antd/lib/menu/SubMenu';
import PrivateRoute from './routes/PrivateRoute';
import PublicRoute from './routes/PublicRoute';
import ParcelHDList from './components/ParcelHDList.component';
import ParcelDTList from './components/ParcelDTList.component';
import ParcelCaveatList from './components/ParcelCaveatList.component';
import 'antd/dist/antd.css';
import Overview from './pages/Overview';

const { Header, Content, Footer } = Layout;


export default function App() {

  // const handleSignout = () => {
  //   logout();
  //   window.location.href = "/"
  // }

  return (
    <Router>
      <Switch>
        <Fragment>
          <Layout className="layout" >
            <Affix>
              <Header>
                <div className="logo" />

                <Menu theme="dark" mode="horizontal" defaultSelectedKeys={['1']}>
                  <Menu.Item key={1}>
                    <Link style={{ textDecoration: "none" }} to="/hd">ยอดสรุปข้อผิดพลาด</Link>
                  </Menu.Item>
                  {/* <SubMenu key={5} style={{ marginLeft: "auto" }} >
                    <Menu.ItemGroup title="Ronnakorn Hompoa">
                      <Menu.Item key="profile">Profile</Menu.Item>
                      <Menu.Item key="signout" style={{color:"red"}}>Sign Out</Menu.Item>
                    </Menu.ItemGroup>

                  </SubMenu> */}
                </Menu>

              </Header>
            </Affix>
            <Content style={{padding:"25px", backgroundColor:"white"}}>
              <PublicRoute exact path="/overview" component={Overview} />
            </Content>
         
          </Layout>
        </Fragment>
      </Switch>
      <BackTop />
    </Router>

  )
}