/* eslint-disable max-len */
// hashtag/[tag].js
import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useRouter } from 'next/router';
import { END } from 'redux-saga';
import Cookies from 'cookies';

import { LOAD_HASHTAG_POSTS_REQ } from '../../reducers/post';
import PostCard from '../../components/PostCard';
import wrapper from '../../store/configureStore';
import { LOAD_ME_REQ } from '../../reducers/user';
import AppLayout from '../../components/AppLayout';
import setJWTToken from '../../securityUtils/setJWTToken';

const Hashtag = () => {
  const dispatch = useDispatch();
  const router = useRouter();
  const { tag } = router.query;
  const { posts, hasMorePosts, loadPostsLoading, pageIndex } = useSelector((state) => state.post);

  useEffect(() => {
    const onScroll = () => {
      if (window.pageYOffset + document.documentElement.clientHeight > document.documentElement.scrollHeight - 300) {
        if (hasMorePosts && !loadPostsLoading) {
          dispatch({
            type: LOAD_HASHTAG_POSTS_REQ,
            data: { page: pageIndex + 1,
              value: tag },
          });
        }
      }
    };
    window.addEventListener('scroll', onScroll);
    return () => {
      window.removeEventListener('scroll', onScroll);
    };
  }, [posts.length, hasMorePosts, tag, loadPostsLoading]);

  return (
    <AppLayout>
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

  setJWTToken(jwtToken);
  context.store.dispatch({
    type: LOAD_ME_REQ,
  });
  context.store.dispatch({
    type: LOAD_HASHTAG_POSTS_REQ,
    data: {
      value: context.params.tag,
      page: 0,
    },
  });
  context.store.dispatch(END);
  await context.store.sagaTask.toPromise();
});

export default Hashtag;
