varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform vec2 u_tex_dim;

void main () {
    // texture size
    // vec2 sz = vec2 (16, 16);
    // scale 8x
    vec3 step = vec3 (1.0 / 8.0, 1.0 / 8.0, 0.0);
    vec2 tex_pixel = u_tex_dim * v_texCoords - step.xy / 2.0;

    vec2 corner = floor (tex_pixel) + 1.0;
    vec2 frac = min ((corner - tex_pixel) * vec2 (8.0, 8.0), vec2 (1.0, 1.0));

    vec4 c1 = texture2D (u_texture, (floor (tex_pixel + step.zz) + 0.5) / u_tex_dim);
    vec4 c2 = texture2D (u_texture, (floor (tex_pixel + step.xz) + 0.5) / u_tex_dim);
    vec4 c3 = texture2D (u_texture, (floor (tex_pixel + step.zy) + 0.5) / u_tex_dim);
    vec4 c4 = texture2D (u_texture, (floor (tex_pixel + step.xy) + 0.5) / u_tex_dim);

    c1 *=        frac.x  *        frac.y;
    c2 *= (1.0 - frac.x) *        frac.y;
    c3 *=        frac.x  * (1.0 - frac.y);
    c4 *= (1.0 - frac.x) * (1.0 - frac.y);

    gl_FragColor = (c1 + c2 + c3 + c4);
}