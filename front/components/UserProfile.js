import React, { useCallback } from 'react';
import { Button, Card, Avatar } from 'antd';
import { useDispatch, useSelector } from 'react-redux';

import { logoutReqAction } from '../reducers/user';

const UserProfile = () => {
  const dispatch = useDispatch();
  const { me, logoutLoading } = useSelector((state) => state.user);

  const onLogout = useCallback(() => {
    dispatch(logoutReqAction());
  }, []);
  return (
    <Card
      actions={[
        <div key="twitt">
          twitt
          <br />
          {me.Posts.length}
        </div>,
        <div key="followings">
          following
          <br />
          {me.Followings.length}
        </div>,
        <div key="followers">
          follwer
          <br />
          {me.Followers.length}
        </div>,
      ]}
    >
      <Card.Meta
        avatar={<Avatar>{me && me.nickname[0]}</Avatar>}
        title={me && me.nickname}
      />
      <Button onClick={onLogout} loading={logoutLoading}>
        로그아웃
      </Button>
    </Card>
  );
};

UserProfile.propTypes = {};

export default UserProfile;
