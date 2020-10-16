import produce from 'immer';
import { ADD_POST_TO_ME, REMOVE_POST_OF_ME } from './post';

export const initialState = {
  loginLoading: false,
  loginDone: false,
  loginError: null,
  logoutLoading: false,
  logoutDone: false,
  logoutError: null,
  signupLoading: false,
  signupDone: false,
  signupError: null,
  changeNicknameLoading: false,
  changeNicknameDone: false,
  changeNicknameError: null,
  me: null,
  followLoading: false,
  followDone: false,
  followError: null,
  unfollowLoading: false,
  unfollowDone: false,
  unfollowError: null,
  signupData: {},
  loginData: {},
};

export const LOG_IN_REQ = 'LOG_IN_REQ';
export const LOG_IN_SUCCESS = 'LOG_IN_SUCCESS';
export const LOG_IN_FAILURE = 'LOG_IN_FAILURE';
export const LOG_OUT_REQ = 'LOG_OUT_REQ';
export const LOG_OUT_SUCCESS = 'LOG_OUT_SUCCESS';
export const LOG_OUT_FAILURE = 'LOG_OUT_FAILURE';
export const SIGN_UP_REQ = 'SIGN_UP_REQ';
export const SIGN_UP_SUCCESS = 'SIGN_UP_SUCCESS';
export const SIGN_UP_FAILURE = 'SIGN_UP_FAILURE';
export const CHANGE_NICKNAME_REQ = 'CHANGE_NICKNAME_REQ';
export const CHANGE_NICKNAME_SUCCESS = 'CHANGE_NICKNAME_SUCCESS';
export const CHANGE_NICKNAME_FAILURE = 'CHANGE_NICKNAME_FAILURE';
export const UNFOLLOW_REQ = 'UNFOLLOW_REQ';
export const UNFOLLOW_SUCCESS = 'UNFOLLOW_SUCCESS';
export const UNFOLLOW_FAILURE = 'UNFOLLOW_FAILURE';
export const FOLLOW_REQ = 'FOLLOW_REQ';
export const FOLLOW_SUCCESS = 'FOLLOW_SUCCESS';
export const FOLLOW_FAILURE = 'FOLLOW_FAILURE';

const dummyUser = (data) => ({
  ...data,
  nickname: 'hoseok',
  id: 1,
  Posts: [],
  Followings: [],
  Followers: [],
});
export const loginReqAction = (data) => ({
  type: LOG_IN_REQ,
  data,
});

export const logoutReqAction = () => ({
  type: LOG_OUT_REQ,
});

export const singupReqAction = () => ({
  type: SIGN_UP_REQ,
});

const reducer = (state = initialState, action) => produce(state, (draft) => {
  switch (action.type) {
    case LOG_IN_REQ:
      draft.loginLoading = true;
      draft.loginDone = false;
      draft.loginError = null;
      break;
    case LOG_IN_SUCCESS:
      draft.loginLoading = false;
      draft.loginDone = true;
      draft.me = dummyUser(action.data);
      break;
    case LOG_IN_FAILURE:
      draft.loginLoading = false;
      draft.loginDone = false;
      draft.loginError = action.error;
      draft.me = null;
      break;
    case LOG_OUT_REQ:
      draft.logoutLoading = true;
      draft.logoutDone = false;
      draft.logoutError = null;
      break;
    case LOG_OUT_SUCCESS:
      draft.logoutLoading = false;
      draft.logoutDone = true;
      draft.loginDone = false;
      draft.me = null;
      break;
    case LOG_OUT_FAILURE:
      draft.logoutLoading = false;
      draft.logoutError = action.error;
      break;
    case SIGN_UP_REQ:
      draft.signupLoading = true;
      draft.signupDone = false;
      draft.signupError = null;
      break;
    case SIGN_UP_SUCCESS:
      draft.signupLoading = false;
      draft.signupDone = true;
      break;
    case SIGN_UP_FAILURE:
      draft.signupLoading = false;
      draft.signupError = action.error;
      break;
    case CHANGE_NICKNAME_REQ:
      draft.changeNicknameLoading = true;
      draft.changeNicknameDone = false;
      draft.changeNicknameError = null;
      break;
    case CHANGE_NICKNAME_SUCCESS:
      draft.changeNicknameLoading = false;
      draft.changeNicknameDone = true;
      break;
    case CHANGE_NICKNAME_FAILURE:
      draft.changeNicknameLoading = false;
      draft.changeNicknameError = action.error;
      break;
    case UNFOLLOW_REQ:
      draft.unfollowNicknameLoading = true;
      draft.unfollowNicknameDone = false;
      draft.unfollowNicknameError = null;
      break;
    case UNFOLLOW_SUCCESS:
      consolg.log(action);
      draft.unfollowNicknameLoading = false;
      draft.unfollowNicknameDone = true;
      draft.me.Followings = draft.me.Followings.filter((v) => v.id !== action.data);
      break;
    case UNFOLLOW_FAILURE:
      draft.unfollowNicknameLoading = false;
      draft.unfollowNicknameError = action.error;
      break;
    case FOLLOW_REQ:
      draft.followNicknameLoading = true;
      draft.followNicknameDone = false;
      draft.followNicknameError = null;
      break;
    case FOLLOW_SUCCESS:
      draft.followNicknameLoading = false;
      draft.followNicknameDone = true;
      draft.me.Followings.push({ id: action.data });
      break;
    case FOLLOW_FAILURE:
      draft.followNicknameLoading = false;
      draft.followNicknameError = action.error;
      break;
    case ADD_POST_TO_ME:
      draft.me.Posts.unshift({ id: action.data });
      break;
    case REMOVE_POST_OF_ME:
      draft.me.Posts = draft.me.Posts.filter((v) => v.id !== action.data);
      break;
    default:
      break;
  }
});

export default reducer;
