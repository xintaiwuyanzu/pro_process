module.exports = {
    root: true,
    env: {
        node: true
    },
    'extends': [
        'plugin:vue/base',
        'eslint:recommended'
    ],
    parserOptions: {
        parser: '@babel/eslint-parser'
    },
    rules: {
        'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
        'no-unused-vars': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
        'no-empty': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
        'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off'
    }
}
