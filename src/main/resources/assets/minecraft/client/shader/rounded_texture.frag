#version 120

uniform sampler2D texture;
uniform vec2 position;
uniform vec2 size;
uniform float radius;

float roundedBoxSDF(vec2 p, vec2 s, vec4 r) {
    r.xy = (p.x > 0.0) ? r.xy : r.zw;
    r.x  = (p.y > 0.0) ? r.x  : r.y;

    vec2 q = abs(p) - s + r.x;
    return min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - r.x;
}

float rounding(vec2 pos, vec2 size, float radius) {
    return length(max(abs(pos) - size, 0.0)) - radius;
}

void main() {
    vec2 rectHalf = size * .5;
    vec4 col = vec4(texture2D(texture, gl_TexCoord[0].st));

    col.a = 1.0 - roundedBoxSDF(gl_FragCoord.xy - position - size / 2.0, size / 2.0, vec4(radius));

    float smoothedAlpha =  (1.0-smoothstep(0.0, 1.0, rounding(rectHalf - (gl_TexCoord[0].st * size), rectHalf - radius - 1., radius))) * col.a;

    gl_FragColor = vec4(col.rgb, smoothedAlpha);
}