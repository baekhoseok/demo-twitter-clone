import React, { useState, useCallback } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Form, Input, Button } from 'antd';

import useInput from '../hooks/useInput';
import { CHANGE_USERNAME_REQ } from '../reducers/user';

const UsernameEditForm = () => {
  const dispatch = useDispatch();
  const { me, changeUsernameLoading } = useSelector((state) => state.user);
  const [editedName, setEditedName] = useState('');

  const onChangeNickname = useCallback((e) => {
    setEditedName(e.target.value);
  }, [editedName]);

  const onEditNickname = useCallback((e) => {
    e.preventDefault();
    dispatch({
      type: CHANGE_USERNAME_REQ,
      data: editedName,
    });
  }, [editedName]);

  return (
    <Form style={{ marginBottom: '20px', border: '1px solid #d9d9d9', padding: '20px' }} onSubmit={onEditNickname}>
      <Input addonBefore="Username" value={editedName || (me && me.username)} onChange={onChangeNickname} />
      <Button type="primary" htmlType="submit" loading={changeUsernameLoading} onClick={onEditNickname}>Edit</Button>
    </Form>
  );
};

export default UsernameEditForm;
