// post/[id].js
import React from 'react';
import { useRouter } from 'next/router';
import { END } from 'redux-saga';
import { useSelector } from 'react-redux';
import Head from 'next/head';
import Cookies from 'cookies';

import wrapper from '../../store/configureStore';
import { LOAD_ME_REQ } from '../../reducers/user';
import { LOAD_POST_REQ } from '../../reducers/post';
import AppLayout from '../../components/AppLayout';
import PostCard from '../../components/PostCard';
import setJWTToken from '../../securityUtils/setJWTToken';

const Post = () => {
  const router = useRouter();
  const { id } = router.query;
  const { singlePost } = useSelector((state) => state.post);

  // if (router.isFallback) {
  //   return <div>로딩중...</div>;
  // }

  return (
    <AppLayout>
      <Head>
        <title>
          {singlePost.account.username}
          님의 글
        </title>
        <meta name="description" content={singlePost.content} />
        <meta property="og:title" content={`${singlePost.account.username}님의 게시글`} />
        <meta property="og:description" content={singlePost.content} />
        <meta property="og:image" content={singlePost.images[0] ? singlePost.images[0].src : 'https://nodebird.com/favicon.ico'} />
        <meta property="og:url" content={`https://nodebird.com/post/${id}`} />
      </Head>
      <PostCard post={singlePost} />
    </AppLayout>
  );
};

// export async function getStaticPaths() {
//   return {
//     paths: [
//       { params: { id: '1' } },
//       { params: { id: '2' } },
//       { params: { id: '3' } },
//     ],
//     fallback: true,
//   };
// }

export const getServerSideProps = wrapper.getServerSideProps(async (context) => {
  const cookies = new Cookies(context.req, context.res);
  const jwtToken = cookies.get('jwtToken').replace('+', ' ');
  setJWTToken(jwtToken);
  context.store.dispatch({
    type: LOAD_ME_REQ,
  });
  context.store.dispatch({
    type: LOAD_POST_REQ,
    data: context.params.id,
  });
  context.store.dispatch(END);
  await context.store.sagaTask.toPromise();
});

export default Post;
