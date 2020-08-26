import React, {Component} from 'react';
import './App.css';
import axios from 'axios'

axios.defaults.crossDomain = true;
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

const BASE_URL = window.location.host === "localhost:3000" ? "localhost:8080/" : window.location.host+"/";

export default class App extends Component {

    constructor(props) {
        super(props);
        this.state = {
            sourceUrl: "",
            showUrl: "",
            shortUrl: "",
            hidden: true,
        }
    }

    render() {
        const {hidden, sourceUrl, shortUrl, showUrl} = this.state;
        return (
            <div className="App">
                <header className="App-header">
                    <b>Enter a lone url to make a tiny</b>
                    <div>
                        <input
                            type="text"
                            onChange={this.handelChange}
                            value={sourceUrl}
                        />
                        <button onClick={this.create}>make tinyURL!</button>
                    </div>

                    {hidden ? null :
                        <div>
                            <b>TinyURL was created!</b>
                            <div style={{
                                marginTop: 10
                            }}>The following url:
                            </div>

                            <div>
                                <a>{showUrl}</a>
                            </div>

                            <div style={{
                                marginTop: 30
                            }}>
                                has a length of {showUrl.length} characters in the following tinyURL which has a
                                length
                                of {shortUrl.length} characters;
                            </div>

                            <div style={{
                                marginTop: 10
                            }}>
                                <a style={{
                                    color:"green",
                                }} href={shortUrl}>{shortUrl}</a>
                            </div>
                        </div>
                    }
                </header>
            </div>
        );
    }

    handleResponse = (resp) => {
        this.setState({
            shortUrl: resp.data,
            hidden: false,
            sourceUrl: ""
        })
    };

    handelChange = (e) => {
        this.setState({
            sourceUrl: e.target.value,
            showUrl: e.target.value,
            hidden: true
        })
    };

    create = () => {
        const {sourceUrl} = this.state;
        axios({
            method: "POST",
            url: "https://" + BASE_URL + "api/v1/source_url/" + sourceUrl,
            headers: {
                "Access-Control-Allow-Origin": "*",
                "Access-Control-Allow-Headers": "content-type",
            }
        }).then(response => {
            console.log(response);
            this.handleResponse(response);
        }).catch(e => {
            console.log(e);
        })
    }


}
