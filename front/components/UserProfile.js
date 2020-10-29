import React, { useCallback } from 'react';
import { Button, Card, Avatar } from 'antd';
import { useDispatch, useSelector } from 'react-redux';
import Link from 'next/link';

import { logoutReqAction } from '../reducers/user';

const UserProfile = () => {
  const dispatch = useDispatch();
  const { me, logoutLoading, loadMeLoading } = useSelector((state) => state.user);

  const onLogout = useCallback(() => {
    dispatch(logoutReqAction());
  }, []);
  return (
    <Card
      actions={[
        <div key="twitt">
          <Link href={`/user/${me.id}`}><a>twitt
            <br />
            {me.posts.length}
          </a>
          </Link>
        </div>,
        <div key="followings">
          <Link href="/profile"><a>
            following
            <br />
            {me.followings.length}
          </a>
          </Link>
        </div>,
        <div key="followers">
          <Link href="/profile"><a>
            follwer
            <br />
            {me.followers.length}
          </a>
          </Link>
        </div>,
      ]}
    >
      <Card.Meta
        avatar={<Avatar>{me && me.username[0]}</Avatar>}
        title={me && me.username}
      />
      <Button onClick={onLogout} loading={logoutLoading}>
        로그아웃
      </Button>
    </Card>
  );
};

UserProfile.propTypes = {};

export default UserProfile;
