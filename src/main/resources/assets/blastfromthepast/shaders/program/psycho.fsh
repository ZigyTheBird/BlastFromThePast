#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform float ZigyTime;

out vec4 fragColor;

vec3 palette(float t) {
    vec3 a = vec3(0.5, 0.5, 0.5);
    vec3 b = vec3(0.5, 0.5, 0.5);
    vec3 c = vec3(1.0, 1.0, 1.0);
    vec3 d = vec3(0.00, 0.33, 0.67);

    return a + b*cos( 6.28318*(c*t+d) );
}

void main() {
    float distFromCenter = distance(texCoord, vec2(0.5, 0.5));

    distFromCenter = pow(1. / distFromCenter, 1.1);

    vec3 col = palette(ZigyTime/1.5+distFromCenter);
    col += texture(DiffuseSampler, texCoord).rgb;
    col /= 2.;

    fragColor = vec4(col,1.0);
}
