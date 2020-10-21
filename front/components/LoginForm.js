import React, { useCallback } from 'react';
import { Form, Input, Button } from 'antd';
import { useDispatch, useSelector } from 'react-redux';
import Link from 'next/link';
import styled from 'styled-components';

import useInput from '../hooks/useInput';
import { loginReqAction } from '../reducers/user';

const ButtonWrapper = styled.div`
  margin-top: 10px;
`;

const FromWrapper = styled(Form)`
  padding: 10px;
`;

const LoginForm = () => {
  const dispatch = useDispatch();
  const { loginLoading } = useSelector((state) => state.user);
  const [username, onChangeUsername] = useInput('');
  const [password, onChangePassword] = useInput('');

  const onSubmit = useCallback(() => {
    dispatch(loginReqAction({ username, password }));
  }, [username, password]);

  return (
    <FromWrapper onFinish={onSubmit}>
      <div>
        <label htmlFor="user-username">Username</label>
        <br />
        <Input
          name="user-username"
          value={username}
          onChange={onChangeUsername}
          required
        />
      </div>
      <div>
        <label htmlFor="user-password">Password</label>
        <br />
        <Input
          name="user-password"
          type="password"
          value={password}
          onChange={onChangePassword}
          required
        />
      </div>
      <ButtonWrapper>
        <Button type="primary" htmlType="submit" loading={loginLoading}>
          LOGIN
        </Button>
        <Link href="/signup">
          <a>
            <Button>SIGNUP</Button>
          </a>
        </Link>
      </ButtonWrapper>
    </FromWrapper>
  );
};

export default LoginForm;
