import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Avatar, Card } from 'antd';
import { END } from 'redux-saga';
import Head from 'next/head';
import { useRouter } from 'next/router';
import Cookies from 'cookies';

import axios from 'axios';
import { LOAD_USER_POSTS_REQ } from '../../reducers/post';
import { LOAD_ME_REQ, LOAD_USER_REQ } from '../../reducers/user';
import PostCard from '../../components/PostCard';
import wrapper from '../../store/configureStore';
import AppLayout from '../../components/AppLayout';
import setJWTToken from "../../securityUtils/setJWTToken";

const User = () => {
  const dispatch = useDispatch();
  const router = useRouter();
  const { id } = router.query;
  const { posts, hasMorePosts, loadPostsLoading, pageIndex } = useSelector((state) => state.post);
  const { userInfo, me } = useSelector((state) => state.user);

  useEffect(() => {
    const onScroll = () => {
      if (window.pageYOffset + document.documentElement.clientHeight > document.documentElement.scrollHeight - 300) {
        if (hasMorePosts && !loadPostsLoading) {
          dispatch({
            type: LOAD_USER_POSTS_REQ,
            data: { 
              accountId: id,
              page: pageIndex + 1 
            },
          });
        }
      }
    };
    window.addEventListener('scroll', onScroll);
    return () => {
      window.removeEventListener('scroll', onScroll);
    };
  }, [posts.length, hasMorePosts, id, loadPostsLoading]);
  console.log('userInfo', userInfo);

  return (
    <AppLayout>
      {userInfo && (
        <Head>
          <title>
            {userInfo.username}
            님의 글
          </title>
          <meta name="description" content={`${userInfo.username}님의 게시글`} />
          <meta property="og:title" content={`${userInfo.username}님의 게시글`} />
          <meta property="og:description" content={`${userInfo.username}님의 게시글`} />
          <meta property="og:image" content="https://nodebird.com/favicon.ico" />
          <meta property="og:url" content={`https://nodebird.com/user/${id}`} />
        </Head>
      )}
      {userInfo && (userInfo.id !== me?.id)
        ? (
          <Card
            style={{ marginBottom: 20 }}
            actions={[
              <div key="twit">
                짹짹
                <br />
                {userInfo.postsCount}
              </div>,
              <div key="following">
                팔로잉
                <br />
                {userInfo.followingsCount}
              </div>,
              <div key="follower">
                팔로워
                <br />
                {userInfo.followersCount}
              </div>,
            ]}
          >
            <Card.Meta
              // avatar={<Avatar>{userInfo.username[0]}</Avatar>}
              title={userInfo.username}
            />
          </Card>
        )
        : null}
      {posts.map((c) => (
        <PostCard key={c.id} post={c} />
      ))}
    </AppLayout>
  );
};

export const getServerSideProps = wrapper.getServerSideProps(async (context) => {
  const cookies = new Cookies(context.req, context.res);
  const jwtToken = cookies.get('jwtToken').replace('+', ' ');
  console.log('hashtag jwtToken', jwtToken);
  console.log('context.params', context.params);

  setJWTToken(jwtToken);
  context.store.dispatch({
    type: LOAD_ME_REQ,
  });
  context.store.dispatch({
    type: LOAD_USER_REQ,
    data: context.params.id,
  });
  context.store.dispatch({
    type: LOAD_USER_POSTS_REQ,
    data: { 
      accountId: context.params.id,
      page: 0
    }
  });
 
  context.store.dispatch(END);
  await context.store.sagaTask.toPromise();
});

export default User;
