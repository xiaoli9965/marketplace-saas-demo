/**
 * @see https://umijs.org/zh-CN/plugins/plugin-access
 * */
export default function access(initialState: { currentUser?: API.CurrentUser | undefined }) {
  const {currentUser} = initialState || {};
  return {
    admin: currentUser && currentUser.access === 'admin',
    consoleAdmin: currentUser && ['admin','console_admin'].indexOf(currentUser?.access) !== -1,
  };
}
