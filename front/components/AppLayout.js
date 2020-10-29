import React, { useCallback } from 'react';
import PropTypes from 'prop-types';
import Link from 'next/link';
import { Menu, Input, Row, Col } from 'antd';
import styled, { createGlobalStyle } from 'styled-components';
import { useSelector } from 'react-redux';
import Router from 'next/router';

import LoginForm from './LoginForm';
import UserProfile from './UserProfile';
import useInput from '../hooks/useInput';

const SearchInput = styled(Input.Search)`
  vertical-align: middle;
`;

const Global = createGlobalStyle`
  .ant-row {
    margin-right: 0 !important;
    margin-left: 0 !important;
  }

  .ant-col:first-child {
    padding-left: 0 !important;
  }

  .ant-col:last-child {
    padding-right: 0 !important;
  }
`;

const AppLayout = ({ children }) => {
  const [searchInput, onChangeSearchInput] = useInput('');
  const { me, loadMeLoading } = useSelector((state) => state.user);
  const onSearch = useCallback(() => {
    if (searchInput) {
      Router.push(`/hashtag/${searchInput}`);
    }
  }, [searchInput]);

  return (
    <div>
      <Global />
      <Menu mode="horizontal">
        <Menu.Item>
          <Link href="/">
            <a>Twitter</a>
          </Link>
        </Menu.Item>
        <Menu.Item>
          <Link href="/profile">
            <a>Profile</a>
          </Link>
        </Menu.Item>
        <Menu.Item>
          <SearchInput
            enterButton
            value={searchInput}
            onChange={onChangeSearchInput}
            onSearch={onSearch}
          />
        </Menu.Item>
        {!me && (
          <Menu.Item>
            <Link href="/signup">
              <a>Signup</a>
            </Link>
          </Menu.Item>
        )}

      </Menu>
      <Row gutter={8}>
        <Col xs={24} md={6}>
          {
            // !loadMeLoading ? (me ? <UserProfile /> : <LoginForm />) : null
          }
          {me ? <UserProfile /> : <LoginForm />}
        </Col>
        <Col xs={24} md={12}>
          {children}
        </Col>
        <Col xs={24} md={6}>
          <a
            href="http://www.naver.com"
            target="_blanck"
            rel="noreferrer noopener"
          >
            Made by Hoseok
          </a>
        </Col>
      </Row>
    </div>
  );
};

AppLayout.propTypes = {
  children: PropTypes.node.isRequired,
};

export default AppLayout;
