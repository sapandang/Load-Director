module.exports = {
    base:"/guide/",
    title: 'Chalba',
    themeConfig: {        
        displayAllHeaders: false ,
        nav: [
            { text: 'Home', link: 'https://buglens.com/' },
            // { text: 'Blog', link: '/blog/' }
            { text: 'Github', link: 'https://github.com/sapandang/chalba' }

        ],
        sidebar: [
          {
            title: 'Guide',
            path: '/guide/',
            collapsable: true,
            children: [        
            ]
          },
          {
            title: 'API',
            path: '/api/',
            collapsable: true,
            children: [        
            ]
          }
        ]
    }
}