using Newtonsoft.Json;
using System;
using System.IO;

namespace ReplayClient
{
    class Program
    {
        static void Main(string[] args)
        {
            if (args.Length != 1)
            {
                Console.WriteLine("No parameters");
                return;
            }
            var oriTick = Environment.TickCount;
            var stream = new FileStream(args[0], FileMode.Open, FileAccess.Read);
            var reader = new FortniteReplayReader.ReplayReader();
            var replay = reader.ReadReplay(stream);
            Console.WriteLine("Took " + (Environment.TickCount - oriTick) + " ms");
            var json = JsonConvert.SerializeObject(replay);
            Console.WriteLine(json);
        }
    }
}
