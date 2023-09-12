#version 120

uniform vec2 loc, size;
uniform vec4 color;
uniform float radius;

float rounding(vec2 pos, vec2 size, float radius) {
    return length(max(abs(pos) - size, 0.0)) - radius;
}


void main() {
    vec2 rectHalf = size * .5;
    float smoothedAlpha =  (1.0-smoothstep(0.0, 1.0, rounding(rectHalf - (gl_TexCoord[0].st * size), rectHalf - radius - 1., radius))) * color.a;
    gl_FragColor = vec4(color.rgb, smoothedAlpha);
}