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
import {
  LOAD_POSTS_REQ,
  LOAD_POSTS_SUCCESS,
  LOAD_POSTS_FAILURE,
  LOAD_USER_POSTS_REQ,
  LOAD_USER_POSTS_SUCCESS,
  LOAD_USER_POSTS_FAILURE,
  LOAD_HASHTAG_POSTS_REQ,
  LOAD_HASHTAG_POSTS_SUCCESS,
  LOAD_HASHTAG_POSTS_FAILURE,
  LOAD_POST_REQ,
  LOAD_POST_SUCCESS,
  LOAD_POST_FAILURE,
  ADD_POST_REQ,
  ADD_POST_SUCCESS,
  ADD_POST_FAILURE,
  UPDATE_POST_REQ,
  UPDATE_POST_SUCCESS,
  UPDATE_POST_FAILURE,
  RETWEET_POST_REQ,
  RETWEET_POST_SUCCESS,
  RETWEET_POST_FAILURE,
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
  UPLOAD_IMAGES_REQ,
  UPLOAD_IMAGES_SUCCESS,
  UPLOAD_IMAGES_FAILURE,
  ADD_POST_TO_ME,
  REMOVE_POST_OF_ME,
} from '../reducers/post';

function loadPostsApi(data) {
  return axios.get(`/api/posts/search?page=${data.page || 0}`);
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
  yield takeLatest(LOAD_POSTS_REQ, loadPosts);
}

function loadUserPostsApi(data) {
  return axios.get(`/api/posts/${data.accountId}/search?page=${data.page || 0}`);
}

function* loadUserPosts(action) {
  try {
    const result = yield call(loadUserPostsApi, action.data);
    yield put({
      type: LOAD_USER_POSTS_SUCCESS,
      data: result.data,
    });
  } catch (error) {
    yield put({
      type: LOAD_USER_POSTS_FAILURE,
      error: error.response.data,
    });
  }
}

function* watchLoadUserPosts() {
  yield takeLatest(LOAD_USER_POSTS_REQ, loadUserPosts);
}

function loadHashTagPostsApi(data) {
  return axios.get(`/api/posts/hashtag/search?value=${encodeURIComponent(data.value)}&page=${data.page || 0}`);
}

function* loadHashTagPosts(action) {
  try {
    const result = yield call(loadHashTagPostsApi, action.data);
    yield put({
      type: LOAD_HASHTAG_POSTS_SUCCESS,
      data: result.data,
    });
  } catch (error) {
    yield put({
      type: LOAD_HASHTAG_POSTS_FAILURE,
      error: error.response.data,
    });
  }
}

function* watchLoadHashTagPosts() {
  yield takeLatest(LOAD_HASHTAG_POSTS_REQ, loadHashTagPosts);
}

function loadPostApi(data) {
  return axios.get(`/api/post/${data}`);
}

function* loadPost(action) {
  try {
    const result = yield call(loadPostApi, action.data);
    yield put({
      type: LOAD_POST_SUCCESS,
      data: result.data,
    });
  } catch (error) {
    yield put({
      type: LOAD_POST_FAILURE,
      error: error.response.data,
    });
  }
}

function* watchLoadPost() {
  yield takeLatest(LOAD_POST_REQ, loadPost);
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
      error: error.response.data,
    });
  }
}

function* watchAddPost() {
  yield takeLatest(ADD_POST_REQ, addPost);
}

function updatePostApi(data) {
  return axios.put(`/api/post/${data.postId}`, { content: data.content });
}

function* updatePost(action) {
  try {
    const result = yield call(updatePostApi, action.data);
    yield put({
      type: UPDATE_POST_SUCCESS,
      data: result.data,
    });
  } catch (error) {
    yield put({
      type: UPDATE_POST_FAILURE,
      error: error.response.data,
    });
  }
}

function* watchUpdatePost() {
  yield takeLatest(UPDATE_POST_REQ, updatePost);
}

function retweetPostApi(data) {
  return axios.post(`/api/posts/${data}/retweet`);
}

function* retweetPost(action) {
  try {
    const result = yield call(retweetPostApi, action.data);
    yield put({
      type: RETWEET_POST_SUCCESS,
      data: result.data,
    });
    yield put({
      type: ADD_POST_TO_ME,
      data: result.data.id,
    });
  } catch (error) {
    yield put({
      type: RETWEET_POST_FAILURE,
      error: error.response.data,
    });
  }
}

function* watchRetweetPost() {
  yield takeLatest(RETWEET_POST_REQ, retweetPost);
}

function removePostApi(data) {
  return axios.delete(`/api/posts/${data}`);
}

function* removePost(action) {
  try {
    const result = yield call(removePostApi, action.data);
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
      error: error.response.data,
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

function uploadImagesApi(data) {
  return axios.post('/api/posts/images', data);
}

function* uploadImages(action) {
  try {
    const result = yield call(uploadImagesApi, action.data);
    yield put({
      type: UPLOAD_IMAGES_SUCCESS,
      data: result.data,
    });
  } catch (error) {
    yield put({
      type: UPLOAD_IMAGES_FAILURE,
      error: error.response.data,
    });
  }
}

function* watchUploadImages() {
  yield takeLatest(UPLOAD_IMAGES_REQ, uploadImages);
}

export default function* postSaga() {
  yield all([
    fork(watchAddPost),
    fork(watchUpdatePost),
    fork(watchRetweetPost),
    fork(watchAddComment),
    fork(watchRemovePost),
    fork(watchLoadPosts),
    fork(watchLoadUserPosts),
    fork(watchLoadHashTagPosts),
    fork(watchLoadPost),
    fork(watchLikePost),
    fork(watchUploadImages),
    fork(watchUnLikePost),
  ]);
}
