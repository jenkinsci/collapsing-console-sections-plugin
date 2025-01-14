import fs from "node:fs";
import path from "node:path";
import process from "node:process";

import stylelintToCheckstyle from "stylelint-checkstyle-reporter";
import stylelint from "stylelint";

export default {
    extends: "stylelint-config-standard",
    rules: {
        // Copied from Jenkins 2.440.3 war/.stylelintrc.js
        "no-descending-specificity": null,
        "selector-class-pattern": "[a-z]",
        "selector-id-pattern": "[a-z]",
        "custom-property-pattern": "[a-z]",
        "value-keyword-case": [
            "lower",
            {
                camelCaseSvgKeywords: true,
            },
        ],
        "property-no-vendor-prefix": null,
        "at-rule-no-unknown": [
            true,
            {
                ignoreAtRules: [
                    "function",
                    "if",
                    "each",
                    "include",
                    "mixin",
                    "for",
                    "use",
                ],
            },
        ],
        "color-function-notation": "legacy",
        "alpha-value-notation": "number",
        "number-max-precision": 5,
        "function-no-unknown": null,
        "no-duplicate-selectors": null,
        "hue-degree-notation": "number",
    },
    formatter: (...params) => {
        // Emit report to console for interactive usage
        stylelint.formatters.string.then(
            stringFormatter => console.log(stringFormatter(...params)),
        );

        // And a CheckStyle report for CI
        // See https://github.com/jenkins-infra/pipeline-library/
        const outputFile = "target/stylelint/checkstyle-result.xml";
        fs.mkdirSync(path.dirname(outputFile), { recursive: true });
        fs.writeFileSync(
            outputFile,
            stylelintToCheckstyle(...params),
        );
        process.stderr.write(`Wrote checkstyle report: ${outputFile}\n`);
    },
};
