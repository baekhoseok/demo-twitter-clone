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
  LOG_IN_REQ,
  LOG_IN_SUCCESS,
  LOG_IN_FAILURE,
  LOG_OUT_REQ,
  LOG_OUT_SUCCESS,
  LOG_OUT_FAILURE,
  SIGN_UP_REQ,
  SIGN_UP_SUCCESS,
  SIGN_UP_FAILURE,
  UNFOLLOW_REQ,
  UNFOLLOW_SUCCESS,
  UNFOLLOW_FAILURE,
  FOLLOW_REQ,
  FOLLOW_SUCCESS,
  FOLLOW_FAILURE,
} from '../reducers/user';
import setJWTToken from '../securityUtils/setJWTToken';

function loginApi(data) {
  return axios.post('/api/accounts/login', data);
}

function* login(action) {
  try {
    const result = yield call(loginApi, action.data);
    yield put({
      type: LOG_IN_SUCCESS,
      data: result.data,
    });
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
    console.log(result);
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

export default function* userSaga() {
  yield all([fork(watchLogIn), fork(watchLogOut), fork(watchSignup),
    fork(watchUnfollow), fork(watchFollow)]);
}
