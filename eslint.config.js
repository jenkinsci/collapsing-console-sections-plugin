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
];
