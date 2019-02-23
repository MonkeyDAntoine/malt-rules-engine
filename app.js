/////////////
// IMPORTS //
/////////////

// server
const express = require('express')
const bodyParser = require('body-parser')
// time utils
const duration = require('timestring')
const moment = require('moment')
// ip locator
const ipstack = require('ipstack')
// rule engine
const Engine = require('json-rules-engine').Engine

////////////////
// INITIALIZE //
////////////////

// configure server
const app = express()
app.use(bodyParser.json()) // support json encoded bodies
app.use(bodyParser.urlencoded({extended: true})) // support encoded bodies

// configure rule engine
const engine = new Engine()
engine.on('success', function(event, almanac, ruleResult) {
    engine.stop();
})

// to add facts to the engine
function fact(name, jsonElement, evaluate) {
    engine.addFact(name, function (params, almanac) {
        return almanac.factValue(jsonElement)
            .then(evaluate)
            .catch(e => {
                return undefined
            })
    })
}

// to get country from IP
function countryFromIP(ip) {
    return new Promise((resolve, reject) => {
        ipstack(ip, '72ed65708b74ff430bde3a813f163cd3', (err, response) => {
            if (err) {
                reject(err)
            } else {
                resolve(response['country_code'])
            }
        })
    })
}

// create operator to compare duration like '1day', '2months'
engine.addOperator('durationGreaterThanInclusive', (factValue, jsonValue) => {
    if (!factValue) return false
    return duration(factValue) >= duration(jsonValue)
})
// create facts that can be used in rules
fact('@mission.duration', 'mission', mission => mission.duration)
fact('@commercialrelation.duration', 'commercialrelation', cr => moment(cr.last_mission).diff(moment(cr.firstmission)) + 'ms')

fact('@client.location', 'client', client => countryFromIP(client.ip))
fact('@freelance.location', 'freelance', freelance => countryFromIP(freelance.ip))

////////////////
// CREATE API //
////////////////

// rules database
let rules = {}
// return all known rules
app.get('/rules', (req, res) => {
    res.json(rules)
})
// return the rule given its id
app.get('/rule/:ruleId', (req, res) => {
    let ruleId = req.param('ruleId')
    let rule = rules[ruleId]
    if (rule) {
        return res.json(rule)
    } else {
        return res.status(404).json({error: 'Rule with ' + ruleId + ' does not exists'})
    }
})
// add a rule in the engine using configured facts
app.post('/rule', (req, res) => {
    try {
        let newRule = req.body
        let engineRule = {}
        engineRule['conditions'] = newRule.restrictions
        engineRule['event'] = {
            type: newRule.name,
            params: {
                percent: newRule.rate.percent
            }
        }
        engineRule['priority'] = 100 - newRule.rate.percent
        engine.addRule(engineRule)
        rules[newRule.id] = newRule
    } catch (e) {
        return res.status(400).json({'error': e.toString()})
    }
    return res.json(req.body)
})
// test all the rules with the request body as an input in the Engine
app.post('/check/fees', (req, res) => {
    let fees = 10 // by default
    let rule = 'Default value rule'
    try {
        return engine
            .run(req.body)
            .then(events => {
                // run() returns events with truthy conditions
                events.forEach(event => {
                    if (event.params.percent < fees) {
                        fees = event.params.percent
                        rule = event.type
                    }
                })
                return res.json({
                    fees: fees,
                    rule: rule
                })
            })
    } catch (e) {
        return res.status(400).json({'error': e.toString()})
    }
})

////////////////
// RUN SERVER //
////////////////
app.listen(3000, () => {
    console.log('Server running on port 3000')
})
