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
import Cookies from 'cookies';
import {
  LOG_IN_REQ,
  LOG_IN_SUCCESS,
  LOG_IN_FAILURE,
  LOG_OUT_REQ,
  LOG_OUT_SUCCESS,
  LOG_OUT_FAILURE,
  LOAD_ME_REQ,
  LOAD_ME_SUCCESS,
  LOAD_ME_FAILURE,
  LOAD_USER_REQ,
  LOAD_USER_SUCCESS,
  LOAD_USER_FAILURE,
  SIGN_UP_REQ,
  SIGN_UP_SUCCESS,
  SIGN_UP_FAILURE,
  CHANGE_USERNAME_REQ,
  CHANGE_USERNAME_SUCCESS,
  CHANGE_USERNAME_FAILURE,
  UNFOLLOW_REQ,
  UNFOLLOW_SUCCESS,
  UNFOLLOW_FAILURE,
  FOLLOW_REQ,
  FOLLOW_SUCCESS,
  FOLLOW_FAILURE,
  REMOVE_FOLLOWER_REQ,
  REMOVE_FOLLOWER_SUCCESS,
  REMOVE_FOLLOWER_FAILURE,
} from '../reducers/user';
import setJWTToken from '../securityUtils/setJWTToken';

function loginApi(data) {
  return axios.post('/api/accounts/login', data, { withCredentials: true });
}

function* login(action) {
  try {
    const result = yield call(loginApi, action.data);
    yield put({
      type: LOG_IN_SUCCESS,
      data: result.data,
    });
    console.log('login result', result);
    localStorage.setItem('jwtToken', result.data.token);
    // set our token in header ***
    setJWTToken(result.data.token);
  } catch (error) {
    yield put({
      type: LOG_IN_FAILURE,
      error: error.response.data,
    });
  }
}

function* watchLogIn() {
  yield takeLatest(LOG_IN_REQ, login);
}

function loadMeApi() {
  return axios.get('/api/accounts/me');
}

function* loadMe() {
  try {
    const result = yield call(loadMeApi);
    yield put({
      type: LOAD_ME_SUCCESS,
      data: result.data,
    });
    // localStorage.setItem('jwtToken', result.data.token);
    // setJWTToken(result.data.token);
  } catch (error) {
    yield put({
      type: LOAD_ME_FAILURE,
      error: error.response.data,
    });
  }
}

function* watchLoadMe() {
  yield takeLatest(LOAD_ME_REQ, loadMe);
}

function loadUserApi(accountId) {
  return axios.get(`/api/accounts/id/${accountId}`);
}

function* loadUser(action) {
  try {
    const result = yield call(loadUserApi, action.data);
    yield put({
      type: LOAD_USER_SUCCESS,
      data: result.data,
    });
  } catch (error) {
    yield put({
      type: LOAD_USER_FAILURE,
      error: error.response.data,
    });
  }
}

function* watchLoadUser() {
  yield takeLatest(LOAD_USER_REQ, loadUser);
}

function logoutApi() {
  axios.post('/api/logout');
}
function* logout() {
  try {
    // const result = call(logoutApi);
    yield delay(500);
    yield put({
      type: LOG_OUT_SUCCESS,
      //   data: result.data
    });
  } catch (error) {
    yield put({
      type: LOG_OUT_FAILURE,
      data: error.response.data,
    });
  }
}

function* watchLogOut() {
  yield takeLatest(LOG_OUT_REQ, logout);
}

function signupApi(data) {
  return axios.post('/api/accounts/signup', data);
}
function* signup(action) {
  try {
    const result = yield call(signupApi, action.data);
    yield put({
      type: SIGN_UP_SUCCESS,
      data: result.data,
    });
  } catch (error) {
    yield put({
      type: SIGN_UP_FAILURE,
      error: error.response.data,
    });
  }
}

function* watchSignup() {
  yield takeLatest(SIGN_UP_REQ, signup);
}

function unfollowApi(data) {
  return axios.post(`/api/accounts/unFollow/${data}`);
}
function* unfollow(action) {
  try {
    const result = yield call(unfollowApi, action.data);
    yield put({
      type: UNFOLLOW_SUCCESS,
      data: result.data,
    });
  } catch (error) {
    yield put({
      type: UNFOLLOW_FAILURE,
      data: error.response.data,
    });
  }
}

function* watchUnfollow() {
  yield takeLatest(UNFOLLOW_REQ, unfollow);
}

function followApi(data) {
  return axios.post(`/api/accounts/follow/${data}`);
}
function* follow(action) {
  try {
    const result = yield call(followApi, action.data);
    yield put({
      type: FOLLOW_SUCCESS,
      data: result.data,
    });
  } catch (error) {
    yield put({
      type: FOLLOW_FAILURE,
      error: error.response.data,
    });
  }
}

function* watchFollow() {
  yield takeLatest(FOLLOW_REQ, follow);
}

function changeUsernameApi(data) {
  return axios.put(`/api/accounts/${data}`);
}
function* changeUsername(action) {
  try {
    const result = yield call(changeUsernameApi, action.data);
    yield put({
      type: CHANGE_USERNAME_SUCCESS,
      data: result.data,
    });
  } catch (error) {
    yield put({
      type: CHANGE_USERNAME_FAILURE,
      error: error.response.data,
    });
  }
}

function* watchChangeUsername() {
  yield takeLatest(CHANGE_USERNAME_REQ, changeUsername);
}

function removeFollowerApi(data) {
  return axios.delete(`/api/accounts/follower/${data}`);
}
function* removeFollower(action) {
  try {
    const result = yield call(removeFollowerApi, action.data);
    yield put({
      type: REMOVE_FOLLOWER_SUCCESS,
      data: result.data,
    });
  } catch (error) {
    yield put({
      type: REMOVE_FOLLOWER_FAILURE,
      error: error.response.data,
    });
  }
}

function* watchRemoveFollower() {
  yield takeLatest(REMOVE_FOLLOWER_REQ, removeFollower);
}

export default function* userSaga() {
  yield all([
    fork(watchLogIn),
    fork(watchLogOut),
    fork(watchSignup),
    fork(watchUnfollow),
    fork(watchFollow),
    fork(watchChangeUsername),
    fork(watchRemoveFollower),
    fork(watchLoadMe),
    fork(watchLoadUser),
  ]);
}
