const apps = [
  {
    path: '/admin/saasUser',
    name: '用户列表',
    access: 'consoleAdmin',
    component: './SaasUser',
  },
  {
    path: '/admin/app',
    name: '接入APP',
    access: 'admin',
    component: './ThirdApp',
  },
  {
    path: '/admin/spi',
    name: 'spi记录',
    access: 'admin',
    component: './SpiRecord',
  },
  {
    path: '/admin/instance',
    name: '实例列表',
    access: 'consoleAdmin',
    component: './Instance',
  }, {
    path: '/admin/instanceRenew',
    name: '续费记录',
    access: 'consoleAdmin',
    component: './InstanceRenew',
  }
]

export default [
  {
    path: '/user',
    layout: false,
    routes: [
      {
        path: '/user',
        routes: [
          {
            name: 'login',
            path: '/user/login',
            component: './user/Login',
          },
        ],
      },
    ],
  },
  {
    path: '/welcome',
    name: 'welcome',
    icon: 'smile',
    component: './Welcome',
  },
  {
    path: '/admin',
    name: 'admin',
    icon: 'crown',
    access: 'consoleAdmin',
    routes: [
      ...apps,
    ],
  },
  {
    path: '/sso/auth',
    hideInMenu: true,
    layout: false,
    name: '免密登录',
    component: './Sso',
  },
  {
    path: '/',
    redirect: '/welcome',
  },
  {
    component: './404',
  },
];
