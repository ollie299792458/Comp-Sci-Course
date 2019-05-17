import sys
import time

from mininet.log import info, setLogLevel
from .test_framework import TestFramework

#    This file is part of Software IPv6 Router in Rust.
#
#    Software IPv6 Router in Rust is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    Software IPv6 Router in Rust is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with Software IPv6 Router in Rust.  If not, see <https://www.gnu.org/licenses/>.
#
#    Copyright 2018,2019 Oliver Black

def run():
    test = TestFramework()

    left_address = 'fc00::1'
    right_address = 'fc00::2'

    default = test.addDefault('d0', 'fc00::', mac='ff:00:00:00:00:00')
    left = test.addIPv6Host('h1', left_address, mac='00:00:00:00:01:00')
    right = test.addIPv6Host('h2', right_address, mac='00:00:00:00:02:00')
    router = test.addRouter('r3', 'fc00::3', mac='00:00:00:00:03:00')

    test.addLink(default, router, addr2='00:00:00:00:03:00')
    test.addLink(left, router, addr1='00:00:00:00:01:00', addr2='00:00:00:00:03:01')
    test.addLink(right, router, addr1='00:00:00:00:02:00', addr2='00:00:00:00:03:02')

    test.start()
    info('11212 hop_limit test starting\n')

    #default.cmdPrint("ifconfig")

    router_process = test.runRouter(router)

    server_process = right.popen(["./rust/luxingfu/target/debug/luxingfu", "h2-eth0"], stdout=sys.stdout, stderr=sys.stdout,
                                 shell=True)

    time.sleep(1)

    client_process = left.popen(["./rust/luxingke/target/debug/luxingke", "h1-eth0", "11212",
                                 '00:00:00:00:01:00',left_address, right_address], stdout=sys.stdout, stderr=sys.stdout,
                                shell=True)



    time.sleep(1)

    #client_process = right.popen([sys.executable,"./python/test/testing_tools/test_client.py", '11211', right_address,
     #                             left_address], stdout=sys.stdout, stderr=sys.stdout, shell=True)
    #time.sleep(1)

    info('Test completed: if two packets dropped due to hop limit then two with 9 and 1 as 8th octect, then success\n')
    server_process.kill()
    test.stop()


if __name__ == '__main__':
    setLogLevel('info')
    run()