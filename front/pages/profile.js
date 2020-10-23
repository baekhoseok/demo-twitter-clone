import React, { useEffect } from 'react';
import Head from 'next/head';
import { useSelector } from 'react-redux';

import Router from 'next/router';
import AppLayout from '../components/AppLayout';
import UsernameEditForm from '../components/UsernameEditForm';
import FollowList from '../components/FollowList';

const Profile = () => {
  const { me } = useSelector((state) => state.user);
  useEffect(() => {
    if (!(me && me.id)) {
      Router.push('/');
    }
  }, [me && me.id]);
  if (!me) {
    return null;
  }
  return (
    <>
      <Head>
        <title>Twitter Profile</title>
      </Head>
      <AppLayout>
        <UsernameEditForm />
        <FollowList header="following" data={me.Followings} />
        <FollowList header="follower" data={me.Followers} />
      </AppLayout>
    </>
  );
};

export default Profile;
