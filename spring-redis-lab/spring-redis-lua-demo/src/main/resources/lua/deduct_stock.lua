---@diagnostic disable: undefined-global
-- Redis Lua 脚本：原子性扣减库存
-- KEYS[1]: 库存 Key
-- ARGV[1]: 扣减数量
-- 返回值: 1 成功, 0 失败

local stockKey = KEYS[1]
local amount = tonumber(ARGV[1])
local currentStock = tonumber(redis.call('get', stockKey) or '0')

if currentStock >= amount then
    redis.call('decrby', stockKey, amount)
    return 1
else
    return 0
end
