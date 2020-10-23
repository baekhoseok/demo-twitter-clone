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
  changeUsernameLoading: false,
  changeUsernameDone: false,
  changeUsernameError: null,
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
export const CHANGE_USERNAME_REQ = 'CHANGE_USERNAME_REQ';
export const CHANGE_USERNAME_SUCCESS = 'CHANGE_USERNAME_SUCCESS';
export const CHANGE_USERNAME_FAILURE = 'CHANGE_USERNAME_FAILURE';
export const UNFOLLOW_REQ = 'UNFOLLOW_REQ';
export const UNFOLLOW_SUCCESS = 'UNFOLLOW_SUCCESS';
export const UNFOLLOW_FAILURE = 'UNFOLLOW_FAILURE';
export const FOLLOW_REQ = 'FOLLOW_REQ';
export const FOLLOW_SUCCESS = 'FOLLOW_SUCCESS';
export const FOLLOW_FAILURE = 'FOLLOW_FAILURE';

const dummyUser = (data) => ({
  ...data,
  username: 'pepe',
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
      draft.me = action.data;

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
      console.log(draft.signupError);
      break;
    case CHANGE_USERNAME_REQ:
      draft.changeUsernameLoading = true;
      draft.changeUsernameDone = false;
      draft.changeUsernameError = null;
      break;
    case CHANGE_USERNAME_SUCCESS:
      draft.changeUsernameLoading = false;
      draft.changeUsernameDone = true;
      break;
    case CHANGE_USERNAME_FAILURE:
      draft.changeUsernameLoading = false;
      draft.changeUsernameError = action.error;
      break;
    case UNFOLLOW_REQ:
      draft.unfollowLoading = true;
      draft.unfollowDone = false;
      draft.unfollowError = null;
      break;
    case UNFOLLOW_SUCCESS:
      console.log('UNFOLLOW_SUCCESS BEGIN', action.data);
      draft.unfollowLoading = false;
      draft.unfollowDone = true;
      draft.me.followings = draft.me.followings.filter((v) => v.id !== action.data.id);
      console.log('UNFOLLOW_SUCCESS END', action.data);
      break;
    case UNFOLLOW_FAILURE:
      draft.unfollowLoading = false;
      draft.unfollowError = action.error;
      break;
    case FOLLOW_REQ:
      draft.followLoading = true;
      draft.followDone = false;
      draft.followError = null;
      break;
    case FOLLOW_SUCCESS:
      console.log('FOLLOW_SUCCESS BEGIN', action.data);

      draft.followLoading = false;
      draft.followDone = true;
      draft.me.followings.push({ id: action.data.id });
      console.log('FOLLOW_SUCCESS END', action.data);
      break;
    case FOLLOW_FAILURE:
      draft.followNicknameLoading = false;
      draft.followNicknameError = action.error;
      break;
    case ADD_POST_TO_ME:
      draft.me.posts.unshift({ id: action.data });
      break;
    case REMOVE_POST_OF_ME:
      draft.me.posts = draft.me.posts.filter((v) => v.id !== action.data);
      break;
    default:
      break;
  }
});

export default reducer;
