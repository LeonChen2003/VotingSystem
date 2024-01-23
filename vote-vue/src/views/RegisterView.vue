<template>
    <div class="background">
        <div style="position:relative; z-index:0">
            <canvas id="stars"></canvas>
        </div>
        <div class="box">
            <div class="sign">
                <h2>注册</h2>
                <div class="inputbox">
                    <input type="text" required="required" v-model="username">
                    <span>用户名</span>
                    <i></i>
                </div>

                <div class="inputbox">
                    <input type="password" required="required" v-model="password">
                    <span>密码</span>
                    <i></i>
                </div>
                <div class="inputbox">
                    <input type="password" required="required" v-model="passwordToken">
                    <span>确认密码</span>
                    <i></i>
                </div>
                <button class="button" @click="register">注册</button>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
function User(id, password) {
    this.id = id;
    this.password = password;
}
export default {
    data() {
        return {
            username: '',
            password: '',
            passwordToken: '',
        }
    },
    mounted() {
        const stars = document.getElementById('stars');
        const context = stars.getContext('2d');

        const windowWidth = window.innerWidth;
        const windowHeight = window.innerHeight;
        stars.width = windowWidth;
        stars.height = windowHeight;

        this.createStars(context);
        this.createMeteors(context);
    },
    methods: {
        register: function () {
            if (this.password == this.passwordToken) {
                axios.post("http://localhost:8080/log", new User(this.username, this.password))
                    .then(res => {
                        if (res.data.code == 1) {
                            this.$message({
                                type: 'success',
                                message: '注册成功!'
                            });
                            //计时器，一秒后刷新页面使得数据能够得到重新载入
                            // setTimeout(function () {
                            //     console.log(1);
                            // }, 2000)
                            window.sessionStorage.setItem('id', this.username);
                            this.$router.push({
                                name: 'log',
                            })
                        }
                    })
            } else {
                this.$notify.error({
                    title: '错误',
                    message: '请输入一样的密码'
                });
            }
        },


        //流星雨
        createStars(context) {
            const starCount = 800;
            for (let i = 0; i < starCount; i++) {
                const star = {
                    x: Math.random() * window.innerWidth,
                    y: 5000 * Math.random(),
                    text: '.',
                    color: '',
                    getColor() {
                        const r = Math.random();
                        if (r < 0.5) {
                            this.color = '#333';
                        } else {
                            this.color = 'white';
                        }
                    },
                    draw() {
                        context.fillStyle = this.color;
                        context.fillText(this.text, this.x, this.y);
                    },
                    init() {
                        this.getColor();
                    },
                };
                star.init();
                star.draw();
            }
        },
        createMeteors(context) {
            const rainCount = 20;
            const rains = [];

            class MeteorRain {
                constructor() {
                    this.x = -1;
                    this.y = -1;
                    this.length = -1;
                    this.angle = 30;
                    this.width = -1;
                    this.height = -1;
                    this.speed = 1;
                    this.offset_x = -1;
                    this.offset_y = -1;
                    this.alpha = 1;
                    this.color1 = '';
                    this.color2 = '';

                    this.init();
                }

                init() {
                    this.getPos();
                    this.alpha = 1;
                    this.getRandomColor();
                    const x = Math.random() * 80 + 150;
                    this.length = Math.ceil(x);
                    this.angle = 30;
                    const speed = Math.random() + 0.5;
                    this.speed = Math.ceil(speed);
                    const cos = Math.cos(this.angle * Math.PI / 180);
                    const sin = Math.sin(this.angle * Math.PI / 180);
                    this.width = this.length * cos;
                    this.height = this.length * sin;
                    this.offset_x = this.speed * cos;
                    this.offset_y = this.speed * sin;
                }

                getRandomColor() {
                    const a = Math.ceil(255 - 240 * Math.random());
                    this.color1 = `rgba(${a},${a},${a},1)`;
                    this.color2 = 'black';
                }

                countPos() {
                    this.x = this.x - this.offset_x;
                    this.y = this.y + this.offset_y;
                }

                getPos() {
                    this.x = Math.random() * window.innerWidth;
                    this.y = Math.random() * window.innerHeight;
                }

                draw() {
                    context.save();
                    context.beginPath();
                    context.lineWidth = 1;
                    context.globalAlpha = this.alpha;
                    const line = context.createLinearGradient(this.x, this.y, this.x + this.width, this.y - this.height);
                    line.addColorStop(0, 'white');
                    line.addColorStop(0.3, this.color1);
                    line.addColorStop(0.6, this.color2);
                    context.strokeStyle = line;
                    context.moveTo(this.x, this.y);
                    context.lineTo(this.x + this.width, this.y - this.height);
                    context.closePath();
                    context.stroke();
                    context.restore();
                }

                move() {
                    const x = this.x + this.width - this.offset_x;
                    const y = this.y - this.height;
                    context.clearRect(x - 3, y - 3, this.offset_x + 5, this.offset_y + 5);
                    this.countPos();
                    this.alpha -= 0.002;
                    this.draw();
                }
            }

            for (let i = 0; i < rainCount; i++) {
                const rain = new MeteorRain();
                rains.push(rain);
            }

            const playRains = () => {
                for (let i = 0; i < rainCount; i++) {
                    const rain = rains[i];
                    rain.move();
                    if (rain.y > window.innerHeight) {
                        context.clearRect(rain.x, rain.y - rain.height, rain.width, rain.height);
                        rains[i] = new MeteorRain();
                    }
                }
                requestAnimationFrame(playRains);
            };

            playRains();
        },
    },
}
</script>

<style>
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

body {
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #23242a;
    overflow: hidden;
}

.box {
    position: relative;
    width: 380px;
    height: 420px;
    background: #1c1717;
    transition: all .4s;
    overflow: hidden;
    top: -594px;
    left: 600px;
    z-index: 5;
}

.box::before {
    content: " ";
    position: absolute;
    top: -50%;
    left: -50%;
    width: 380px;
    height: 420px;
    background: linear-gradient(to top, transparent, #45f3ff, #45f3ff);
    transform-origin: bottom right;
    animation: bo 6s linear infinite;
}

.box::after {
    content: " ";
    position: absolute;
    top: -50%;
    left: -50%;
    width: 380px;
    height: 420px;
    background: linear-gradient(to top, transparent, #45f3ff, #45f3ff);
    transform-origin: bottom right;
    animation: bo 6s linear infinite;
    animation-delay: -3s;
}

@keyframes bo {
    0% {}

    100% {
        transform: rotate(360deg);
    }
}

.box .sign {
    position: absolute;
    inset: 2px;
    border-radius: 8px;
    background: #2b2c2e;
    z-index: 10;
    padding: 50px 40px;
    display: flex;
    flex-direction: column;
}

.sign h2 {
    color: #45f3ff;
    font-weight: 500;
    text-align: center;
    letter-spacing: 0.1em;
}

.inputbox {
    position: relative;
    width: 300px;
    margin-top: 35px;
}

.inputbox input {
    position: relative;
    width: 100%;
    padding: 20px 10px 10px;
    background: transparent;
    border: none;
    outline: none;
    color: #23242a;
    font-size: 1em;
    letter-spacing: 0.05em;
    z-index: 10;
}

.inputbox span {
    position: absolute;
    left: 0;
    padding: 20px 0px 10px;
    font-size: 1em;
    color: #8f8f8f;
    pointer-events: none;
    letter-spacing: 0.05em;
    transition: all 0.5s;
}

.inputbox input:valid~span,
.inputbox input:focus~span {
    color: #45f3ff;
    transform: translateX(0px) translateY(-34px);
    font-size: 0.75em;
}

.inputbox i {
    position: absolute;
    left: 0;
    bottom: 0;
    width: 100%;
    height: 2px;
    background: #45f3ff;
    border-radius: 4px;
    transition: 0.5s;
    pointer-events: none;
    z-index: 9;
}

.inputbox input:valid~i,
.inputbox input:focus~i {
    height: 44px;
}

.links {
    display: flex;
    justify-content: space-between;
}

.links a {
    margin: 10px 0;
    font-size: 0.75em;
    color: #8f8f8f;
    text-decoration: none;
}

.links a:hover,
.links a:nth-child(2) {
    color: #45f3ff;
}

/* input[type="submit"] {
    border: none;
    outline: none;
    background: #45f3ff;
    padding: 11px 25px;
    width: 100px;
    margin-top: 20px;
    border-radius: 4px;
    font-weight: 600;
    cursor: pointer;
} */

.button {
    border: none;
    outline: none;
    background: #45f3ff;
    padding: 11px 25px;
    width: 100px;
    margin-top: 20px;
    border-radius: 4px;
    font-weight: 600;
    cursor: pointer;
    display: inline-block;
    position: relative;
    left: 90px;
}

.background {
    float: left;
    background-image: url("../assets/bcg.jpg");
    width: 1536px;
    height: 745px;
    background-repeat: no-repeat;
    background-position: center center;
    background-repeat: no-repeat;
    background-attachment: fixed;
    background-size: cover;
    z-index: -1;
}

.canvas-container {
    position: absolute;
    top: 0;
    left: 0;
    z-index: -1;
    width: 100%;
    height: 100%;
}

.canvas-container canvas {
    width: 100%;
    height: 100%;
}
</style>