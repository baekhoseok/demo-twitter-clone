import React, { useCallback, useState, useEffect } from 'react';
import Head from 'next/head';
import { Form, Input, Checkbox, Button } from 'antd';
import styled from 'styled-components';
import { useDispatch, useSelector } from 'react-redux';

import Router from 'next/router';
import AppLayout from '../components/AppLayout';
import useInput from '../hooks/useInput';
import { SIGN_UP_REQ } from '../reducers/user';

const ErrorMessage = styled.div`
  color: "red";
`;

const Signup = () => {
  const dispatch = useDispatch();
  const { signupLoading, signupDone, signupError } = useSelector((state) => state.user);
  const [email, onChangeEmail] = useInput('');
  const [username, onChangeUsername] = useInput('');
  const [password, onChangePassword] = useInput('');
  const [passwordConfirm, setPasswordConfirm] = useState('');
  const [passwordError, setPasswordError] = useState(false);
  const [term, setTerm] = useState('');
  const [termError, setTermError] = useState(false);

  const onChangePasswordConfirm = useCallback(
    (e) => {
      setPasswordConfirm(e.target.value);
      setPasswordError(e.target.value !== password);
    },
    [password],
  );
  const onChangeTerm = useCallback((e) => {
    setTerm(e.target.checked);
    setTermError(false);
  }, []);
  const onSubmit = useCallback(() => {
    if (password !== passwordConfirm) {
      return setPasswordError(true);
    }

    if (!term) {
      return setTermError(true);
    }
    dispatch({
      type: SIGN_UP_REQ,
      data: { username, email, password, passwordConfirm },
    });
  }, [password, passwordConfirm, term]);

  useEffect(() => {
    if (signupDone) {
      Router.push('/');
    }
  }, [signupDone]);

  useEffect(() => {
    if (signupError) {
      if (signupError.username && signupError.email) {
        alert(`${signupError.username} / ${signupError.email}`);
      } else if (signupError.username) {
        alert(signupError.username);
      } else if (signupError.email) {
        alert(signupError.email);
      }
    }
  }, [signupError]);

  return (
    <>
      <AppLayout>
        <Head>
          <title>Twitter Signup</title>
        </Head>
        <Form onFinish={onSubmit}>
          <div>
            <label htmlFor="user-email">Email</label>
            <br />
            <Input
              name="user-email"
              type="email "
              value={email}
              required
              onChange={onChangeEmail}
            />
          </div>
          <div>
            <label htmlFor="user-username">Username</label>
            <br />
            <Input
              name="user-username"
              value={username}
              required
              onChange={onChangeUsername}
            />
          </div>
          <div>
            <label htmlFor="user-password">Password</label>
            <br />
            <Input
              name="user-password"
              type="password"
              value={password}
              required
              onChange={onChangePassword}
            />
          </div>
          <div>
            <label htmlFor="user-password-confirm">Password Confirm</label>
            <br />
            <Input
              name="user-password-confirm"
              type="password"
              value={passwordConfirm}
              required
              onChange={onChangePasswordConfirm}
            />
            {passwordError && <ErrorMessage>Password not match!!</ErrorMessage>}
          </div>
          <div>
            <Checkbox name="user-term" checked={term} onChange={onChangeTerm}>
              약관에 동의합니다.
            </Checkbox>
            {termError && (
              <div style={{ color: 'red' }}>약관에 동의하셔야 합니다.</div>
            )}
          </div>
          <div style={{ marginTop: 10 }}>
            <Button type="primary" htmlType="submit" loading={signupLoading}>
              가입하기
            </Button>
          </div>
        </Form>
      </AppLayout>
    </>
  );
};

export default Signup;
