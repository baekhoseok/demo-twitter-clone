import {
  all,
  fork,
  takeLatest,
  put,
  call,
  throttle,
  delay,
} from 'redux-saga/effects';
import axios from 'axios';
import shortId from 'shortid';
import {
  LOAD_POSTS_REQ,
  LOAD_POSTS_SUCCESS,
  LOAD_POSTS_FAILURE,
  ADD_POST_REQ,
  ADD_POST_SUCCESS,
  ADD_POST_FAILURE,
  LIKE_POST_REQ,
  LIKE_POST_SUCCESS,
  LIKE_POST_FAILURE,
  UNLIKE_POST_REQ,
  UNLIKE_POST_SUCCESS,
  UNLIKE_POST_FAILURE,
  REMOVE_POST_REQ,
  REMOVE_POST_SUCCESS,
  REMOVE_POST_FAILURE,
  ADD_COMMENT_REQ,
  ADD_COMMENT_SUCCESS,
  ADD_COMMENT_FAILURE,
  ADD_POST_TO_ME,
  REMOVE_POST_OF_ME,
  generateDummyPosts } from '../reducers/post';

function loadPostsApi(data) {
  return axios.get(`/api/posts/search?page=${data.page}`);
}

function* loadPosts(action) {
  try {
    const result = yield call(loadPostsApi, action.data);
    yield put({
      type: LOAD_POSTS_SUCCESS,
      data: result.data,
    });
  } catch (error) {
    yield put({
      type: LOAD_POSTS_FAILURE,
      data: error.response.data,
    });
  }
}

function* watchLoadPosts() {
  yield throttle(5000, LOAD_POSTS_REQ, loadPosts);
}

function addPostApi(data) {
  return axios.post('/api/posts', data);
}

function* addPost(action) {
  try {
    const result = yield call(addPostApi, action.data);
    yield put({
      type: ADD_POST_SUCCESS,
      data: result.data,
    });
    yield put({
      type: ADD_POST_TO_ME,
      data: result.data.id,
    });
  } catch (error) {
    yield put({
      type: ADD_POST_FAILURE,
      data: error.response.data,
    });
  }
}

function* watchAddPost() {
  yield takeLatest(ADD_POST_REQ, addPost);
}

function removePostApi(data) {
  return axios.delete(`/api/posts/${data}`);
}

function* removePost(action) {
  try {
    const result = yield call(removePostApi, action.data);
    console.log(result);
    yield put({
      type: REMOVE_POST_SUCCESS,
      data: result.data,
    });
    yield put({
      type: REMOVE_POST_OF_ME,
      data: result.data.id,
    });
  } catch (error) {
    yield put({
      type: REMOVE_POST_FAILURE,
      data: error.response.data,
    });
  }
}

function* watchRemovePost() {
  yield takeLatest(REMOVE_POST_REQ, removePost);
}

function addCommentApi(data) {
  return axios.post(`/api/posts/${data.postId}/comment`, data);
}

function* addComment(action) {
  try {
    const result = yield call(addCommentApi, action.data);
    yield put({
      type: ADD_COMMENT_SUCCESS,
      data: result.data,
    });
  } catch (error) {
    yield put({
      type: ADD_COMMENT_FAILURE,
      error: error.response.data,
    });
  }
}

function* watchAddComment() {
  yield takeLatest(ADD_COMMENT_REQ, addComment);
}

function likePostApi(data) {
  return axios.post(`/api/posts/${data}/like`);
}

function* likePost(action) {
  try {
    const result = yield call(likePostApi, action.data);
    yield put({
      type: LIKE_POST_SUCCESS,
      data: result.data,
    });
  } catch (error) {
    console.log(error);
    yield put({
      type: LIKE_POST_FAILURE,
      error: error.response.data,
    });
  }
}

function* watchLikePost() {
  yield takeLatest(LIKE_POST_REQ, likePost);
}

function unLikePostApi(data) {
  return axios.delete(`/api/posts/${data}/like`);
}

function* unLikePost(action) {
  try {
    const result = yield call(unLikePostApi, action.data);
    yield put({
      type: UNLIKE_POST_SUCCESS,
      data: result.data,
    });
  } catch (error) {
    yield put({
      type: UNLIKE_POST_FAILURE,
      error: error.response.data,
    });
  }
}

function* watchUnLikePost() {
  yield takeLatest(UNLIKE_POST_REQ, unLikePost);
}

export default function* postSaga() {
  yield all([fork(watchAddPost), fork(watchAddComment), fork(watchRemovePost), fork(watchLoadPosts),
    fork(watchLikePost), fork(watchUnLikePost)]);
}
