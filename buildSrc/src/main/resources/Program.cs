using Newtonsoft.Json;
using System;
using System.IO;

namespace ReplayClient
{
    class Program
    {
        static void Main(string[] args)
        {
            if (args.Length != 2)
            {
                Console.WriteLine("No parameters");
                return;
            }
            var oriTick = Environment.TickCount;
            var stream = new FileStream(args[0], FileMode.Open, FileAccess.Read);
            var reader = new FortniteReplayReader.ReplayReader(null, (Unreal.Core.Models.Enums.ParseMode)Enum.Parse(typeof(Unreal.Core.Models.Enums.ParseMode), args[1]));
            var replay = reader.ReadReplay(stream);
            Console.WriteLine("Took " + (Environment.TickCount - oriTick) + " ms");
            var json = JsonConvert.SerializeObject(replay, new JsonSerializerSettings { ReferenceLoopHandling = ReferenceLoopHandling.Ignore });
            Console.WriteLine(json);
        }
    }
}
