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
  axios.get('/api/posts', data);
}

function* loadPosts(action) {
  try {
    // const result = call(addPostApi, action.data);
    yield delay(1000);
    yield put({
      type: LOAD_POSTS_SUCCESS,
      data: generateDummyPosts(10),
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
  axios.post('/api/post', data);
}

function* addPost(action) {
  try {
    // const result = call(addPostApi, action.data);
    yield delay(1000);
    const id = shortId.generate();
    yield put({
      type: ADD_POST_SUCCESS,
      data: action.data,
    });
    yield put({
      type: ADD_POST_TO_ME,
      data: id,
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
  axios.delete('/api/post', data);
}

function* removePost(action) {
  try {
    // const result = call(removePostApi, action.data);
    yield delay(1000);
    const id = shortId.generate();
    yield put({
      type: REMOVE_POST_SUCCESS,
      data: action.data,
    });
    yield put({
      type: REMOVE_POST_OF_ME,
      data: id,
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
  axios.post(`/api/post/${data.postId}/comment`, data);
}

function* addComment(action) {
  try {
    // const result = call(addPostApi, action.data);
    yield delay(500);
    yield put({
      type: ADD_COMMENT_SUCCESS,
      data: action.data,
    });
  } catch (error) {
    yield put({
      type: ADD_COMMENT_FAILURE,
      data: error.response.data,
    });
  }
}

function* watchAddComment() {
  yield takeLatest(ADD_COMMENT_REQ, addComment);
}

export default function* postSaga() {
  yield all([fork(watchAddPost), fork(watchAddComment), fork(watchRemovePost), fork(watchLoadPosts)]);
}
