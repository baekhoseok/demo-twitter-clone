/* eslint-disable max-len */
import React, { useEffect, useState, useCallback } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { END } from 'redux-saga';
import Cookies from 'cookies';

import AppLayout from '../components/AppLayout';
import PostForm from '../components/PostForm';
import PostCard from '../components/PostCard';
import { LOAD_POSTS_REQ } from '../reducers/post';
import wrapper from '../store/configureStore';
import setJWTToken from '../securityUtils/setJWTToken';
import { LOAD_ME_REQ } from '../reducers/user';
// import setJWTToken from "../securityUtils/setJWTToken";

const Home = () => {
  const dispatch = useDispatch();
  const { me } = useSelector((state) => state.user);
  const { posts, hasMorePost, loadPostsLoading, pageIndex, retweetPostError } = useSelector((state) => state.post);
  const [canReqPosts, setCanReqPosts] = useState(false);
  // useEffect(() => {
  //   const token = localStorage.getItem('jwtToken');
  //   if (token != null) {
  //     setJWTToken(token);
  //     dispatch({
  //       type: LOAD_ME_REQ,
  //     });
  //   }
  // }, []);

  useEffect(() => {
    if (retweetPostError) {
      alert(retweetPostError);
    }
  }, [retweetPostError]);

  useEffect(() => {
    setCanReqPosts(!loadPostsLoading);
    console.log('canReqPosts', canReqPosts);
  }, [loadPostsLoading]);

  const onScroll = useCallback(() => {
    // eslint-disable-next-line max-len
    console.log('hasMorePost', hasMorePost, 'loadPostsLoading', loadPostsLoading, 'canReqPosts', canReqPosts);
    if (window.scrollY + document.documentElement.clientHeight > document.documentElement.scrollHeight - 300) {
      if (hasMorePost && !loadPostsLoading) {
        setCanReqPosts(true);
        dispatch({
          type: LOAD_POSTS_REQ,
          data: { page: pageIndex + 1 },
        });
      }
    }
  }, [hasMorePost, pageIndex, loadPostsLoading]);

  useEffect(() => {
    window.addEventListener('scroll', onScroll);
    return () => {
      window.removeEventListener('scroll', onScroll);
      // countRef.current = [];
    };
  }, [hasMorePost, posts.length]);

  return (
    <AppLayout>
      {me && <PostForm />}
      {posts.map((post) => (
        <PostCard key={post.id} post={post} />
      ))}
    </AppLayout>
  );
};

export const getServerSideProps = wrapper.getServerSideProps(async (context) => {
  const cookies = new Cookies(context.req, context.res);
  console.log('cookies', cookies);
  const jwtToken = cookies.get('jwtToken').replace('+', ' ');
  setJWTToken(jwtToken);
  context.store.dispatch({
    type: LOAD_ME_REQ,
  });
  context.store.dispatch({
    type: LOAD_POSTS_REQ,
    data: { page: 0 },
  });
  context.store.dispatch(END);
  await context.store.sagaTask.toPromise();
});

export default Home;
