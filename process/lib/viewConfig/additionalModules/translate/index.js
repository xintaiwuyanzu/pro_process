import zh from "./zh";

const replace = {TextAnnotation: '文字注释'}
const customTranslate = (template, replacements) => {
    replacements = replacements || {};
    template = zh[template] || template;
    return template.replace(/{([^}]+)}/g, function (_, key) {
        const r = replacements[key]
        if (replace[r]) {
            return replace[r]
        } else {
            return r || '{' + key + '}';
        }
    });
}
export default {
    translate: ['value', customTranslate]
}
