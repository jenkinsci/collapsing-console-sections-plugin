import globals from "globals";
import pluginJs from "@eslint/js";
import stylistic from "@stylistic/eslint-plugin";

export default [
    {
        languageOptions: {
            ecmaVersion: 2022,
            globals: {
                ...globals.browser,
                // From Jenkins eslint.config.cjs
                Behaviour: "readonly",
                rootURL: "readonly",
                iota: "writable",
            },
        },
    },
    pluginJs.configs.recommended,
    stylistic.configs.customize({
        indent: 4,
        quotes: "double",
        semi: true,
        braceStyle: "1tbs",
    }),
    {
        rules: {
            "@stylistic/arrow-parens": "off",
            "no-redeclare": "off",
            "no-undef": "off",
            "no-unused-vars": "off",
            "@stylistic/brace-style": "off",
            "@stylistic/comma-dangle": "off",
            "@stylistic/indent": "off",
            "@stylistic/key-spacing": "off",
            "@stylistic/keyword-spacing": "off",
            "@stylistic/no-mixed-spaces-and-tabs": "off",
            "@stylistic/no-multiple-empty-lines": "off",
            "@stylistic/no-multi-spaces": "off",
            "@stylistic/no-tabs": "off",
            "@stylistic/no-trailing-spaces": "off",
            "@stylistic/padded-blocks": "off",
            "@stylistic/semi": "off",
            "@stylistic/space-before-blocks": "off",
            "@stylistic/space-before-function-paren": "off",
            "@stylistic/spaced-comment": "off",
            "@stylistic/space-infix-ops": "off",
            "@stylistic/space-in-parens": "off",
            "@stylistic/space-unary-ops": "off",
        },
    },
];
